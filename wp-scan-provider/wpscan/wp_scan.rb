#require './lib/wpscan/wpscan_helper'
$: << '.'

$exit_code = 0

require File.join(__dir__, 'lib', 'wpscan', 'wpscan_helper')
class WpScan
	attr_accessor :target,:out_put_file
	def scan
		@scan_results = {findings: []}
		wpscan_options = WpscanOptions.load_from_arguments
		wpscan_options.url = self.target
		Browser.instance(wpscan_options.to_h.merge(max_threads: wpscan_options.threads))
		wp_target = WpTarget.new(wpscan_options.url, wpscan_options.to_h)
		if wp_target.wordpress_hosted?
			@scan_results[:status] = "Failed"
			@scan_results[:message] = "We do not support scanning *.wordpress.com hosted blogs"
		end
		if wp_target.ssl_error?
			@scan_results[:status] = "Failed"
			@scan_results[:message] = "The target site returned an SSL/TLS error"
		end
		unless wp_target.online?
			@scan_results[:status] = "Failed"
			@scan_results[:message] = "The WordPress URL supplied '#{wp_target.uri}' seems to be down"
		end
		if wpscan_options.proxy
			proxy_response = Browser.get(wp_target.url)
			unless WpTarget::valid_response_codes.include?(proxy_response.code)
				@scan_results[:status] = "Failed"
				@scan_results[:message] = "Proxy Error :\r\nResponse Code: #{proxy_response.code}\r\nResponse Headers: #{proxy_response.headers}"
			end
		end
		if (redirection = wp_target.redirection)
			if redirection =~ /\/wp-admin\/install\.php$/
				finding = { severity: "Critical",description: "The Website is not fully configured and currently in install mode. Call it to create a new admin user.",title: "currently in install mode"}
				@scan_results[:findings] << finding
			else
				wpscan_options.url = redirection
				wp_target = WpTarget.new(redirection, wpscan_options.to_h)
			end
		end
		unless wpscan_options.force
			unless wp_target.wordpress?
				@scan_results[:status] = "Failed"
				@scan_results[:message] = "The remote website is up, but does not seem to be running WordPress."
			end
		end
		start_memory = get_memory_usage unless windows?
		if wp_target.has_robots?
			finding = {severity: "Info",title: "robots.txt",description: "robots.txt available under: '#{wp_target.robots_url}'" }
			@scan_results[:findings] << finding
			wp_target.parse_robots_txt.each do |dir|
				finding = {severity: "Info",title: "robots.txt",description: "Interesting entry from robots.txt: #{dir}" }
				@scan_results[:findings] << finding
			end
		end
		if wp_target.has_readme?
			finding = {severity: "High",title: "WordPress file exists exposing a version number",description: "The WordPress '#{wp_target.readme_url}' file exists exposing a version number" }
			@scan_results[:findings] << finding
		end
		if wp_target.has_full_path_disclosure?
			finding = {severity: "High",title: "Full Path Disclosure (FPD)",description: "Full Path Disclosure (FPD) in '#{wp_target.full_path_disclosure_url}': #{wp_target.full_path_disclosure_data}" }
			@scan_results[:findings] << finding
		end
		if wp_target.has_debug_log?
			finding = {severity: "Critical",title: "Debug log file",description: "Debug log file found: #{wp_target.debug_log_url}" }
			@scan_results[:findings] << finding
		end
		wp_target.config_backup.each do |file_url|
			finding = {severity: "Critical",title: "backup file presents",description: "A wp-config.php backup file has been found in: '#{file_url}'"}
			@scan_results[:findings] << finding
		end
		if wp_target.search_replace_db_2_exists?
			finding = {severity: "Critical",title: "searchreplacedb2.php has been found",description: "searchreplacedb2.php has been found in: '#{wp_target.search_replace_db_2_url}'"}
			@scan_results[:findings] << finding
		end
		if wp_target.multisite?
			finding = {severity: "Info",title: "site seems to be a multisite", description: "This site seems to be a multisite (http://codex.wordpress.org/Glossary#Multisite)"}
			@scan_results[:findings] << finding
		end
		if wp_target.has_must_use_plugins?
			finding = {severity: "Info",title: "Must Use Plugins",description: "This site has 'Must Use Plugins' (http://codex.wordpress.org/Must_Use_Plugins)" }
			@scan_results[:findings] << finding
		end

		if wp_target.registration_enabled?
			finding = {severity: "High",title: "Registration is enabled",description: "Registration is enabled: #{wp_target.registration_url}" }
			@scan_results[:findings] << finding
		end

		if wp_target.has_xml_rpc?
			finding = {severity: "Info",title: "XML-RPC Interface available",description: "XML-RPC Interface available under: #{wp_target.xml_rpc_url}" }
			@scan_results[:findings] << finding
		end

		if wp_target.upload_directory_listing_enabled?
			finding = {severity: "High",title: "Upload directory has directory listing enabled",description: "Upload directory has directory listing enabled: #{wp_target.upload_dir_url}" }
			@scan_results[:findings] << finding
		end

		if wp_target.include_directory_listing_enabled?
			finding = {severity: "High",title: "Includes directory has directory listing enabled",description: "Includes directory has directory listing enabled: #{wp_target.includes_dir_url}"}
			@scan_results[:findings] << finding
		end
		enum_options = {
			show_progression: true,
			exclude_content: wpscan_options.exclude_content_based
		}
		if wp_version = wp_target.version(WP_VERSIONS_FILE)
			findings = wp_version.output(wpscan_options.verbose)
			collect_findings(findings)
		else
			#puts
			puts notice('WordPress version can not be detected')
		end
		if wp_theme = wp_target.theme
			#puts
			# Theme version is handled in #to_s
			puts info("WordPress theme in use: #{wp_theme}")
			findings = wp_theme.output(wpscan_options.verbose)
			collect_findings(findings)

			# Check for parent Themes
			parent_theme_count = 0
			while wp_theme.is_child_theme? && parent_theme_count <= wp_theme.parent_theme_limit
				parent_theme_count += 1

				parent = wp_theme.get_parent_theme
				puts
				puts info("Detected parent theme: #{parent}")
				findings = parent.output(wpscan_options.verbose)
				collect_findings(findings)
				wp_theme = parent
			end

		end
		if wpscan_options.enumerate_plugins == nil and wpscan_options.enumerate_only_vulnerable_plugins == nil
			#puts
			puts info('Enumerating plugins from passive detection ...')

			wp_plugins = WpPlugins.passive_detection(wp_target)
			if !wp_plugins.empty?
				if wp_plugins.size == 1
					puts " | #{wp_plugins.size} plugin found:"
				else
					puts " | #{wp_plugins.size} plugins found:"
				end
				findings = wp_plugins.output(wpscan_options.verbose)
				collect_findings(findings)
			else
				puts info('No plugins found')
			end
		end
		# Enumerate the installed plugins
		if wpscan_options.enumerate_plugins or wpscan_options.enumerate_only_vulnerable_plugins or wpscan_options.enumerate_all_plugins
			puts
			if wpscan_options.enumerate_only_vulnerable_plugins
				puts info('Enumerating installed plugins (only ones with known vulnerabilities) ...')
				plugin_enumeration_type = :vulnerable
			end

			if wpscan_options.enumerate_plugins
				puts info('Enumerating installed plugins (only ones marked as popular) ...')
				plugin_enumeration_type = :popular
			end

			if wpscan_options.enumerate_all_plugins
				puts info('Enumerating all plugins (may take a while and use a lot of system resources) ...')
				plugin_enumeration_type = :all
			end
			puts

			wp_plugins = WpPlugins.aggressive_detection(wp_target,
								    enum_options.merge(
									    file: PLUGINS_FILE,
									    type: plugin_enumeration_type
			)
								   )

			puts
			if !wp_plugins.empty?
				puts info("We found #{wp_plugins.size} plugins:")
				findings = wp_plugins.output(wpscan_options.verbose)
				collect_findings(findings)
			else
				puts info('No plugins found')
			end
		end
		# Enumerate the installed plugins
		if wpscan_options.enumerate_plugins or wpscan_options.enumerate_only_vulnerable_plugins or wpscan_options.enumerate_all_plugins
			puts
			if wpscan_options.enumerate_only_vulnerable_plugins
				puts info('Enumerating installed plugins (only ones with known vulnerabilities) ...')
				plugin_enumeration_type = :vulnerable
			end

			if wpscan_options.enumerate_plugins
				puts info('Enumerating installed plugins (only ones marked as popular) ...')
				plugin_enumeration_type = :popular
			end

			if wpscan_options.enumerate_all_plugins
				puts info('Enumerating all plugins (may take a while and use a lot of system resources) ...')
				plugin_enumeration_type = :all
			end
			puts

			wp_plugins = WpPlugins.aggressive_detection(wp_target,
								    enum_options.merge(
									    file: PLUGINS_FILE,
									    type: plugin_enumeration_type
			)
								   )

			puts
			if !wp_plugins.empty?
				puts info("We found #{wp_plugins.size} plugins:")
				findings = wp_plugins.output(wpscan_options.verbose)
				collect_findings(findings)
			else
				puts info('No plugins found')
			end
		end
		# Enumerate installed themes
		if wpscan_options.enumerate_themes or wpscan_options.enumerate_only_vulnerable_themes or wpscan_options.enumerate_all_themes
			puts
			if wpscan_options.enumerate_only_vulnerable_themes
				puts info('Enumerating installed themes (only ones with known vulnerabilities) ...')
				theme_enumeration_type = :vulnerable
			end

			if wpscan_options.enumerate_themes
				puts info('Enumerating installed themes (only ones marked as popular) ...')
				theme_enumeration_type = :popular
			end

			if wpscan_options.enumerate_all_themes
				puts info('Enumerating all themes (may take a while and use a lot of system resources) ...')
				theme_enumeration_type = :all
			end
			puts

			wp_themes = WpThemes.aggressive_detection(wp_target,
								  enum_options.merge(
									  file: THEMES_FILE,
									  type: theme_enumeration_type
			)
								 )
			puts
			if !wp_themes.empty?
				puts info("We found #{wp_themes.size} themes:")
				findings = wp_themes.output(wpscan_options.verbose)
				collect_findings(findings)
			else
				puts info('No themes found')
			end
		end
		if wpscan_options.enumerate_timthumbs
			puts
			puts info('Enumerating timthumb files ...')
			puts

			wp_timthumbs = WpTimthumbs.aggressive_detection(wp_target,
									enum_options.merge(
										file: DATA_DIR + '/timthumbs.txt',
										theme_name: wp_theme ? wp_theme.name : nil
			)
								       )
			puts
			if !wp_timthumbs.empty?
				puts info("We found #{wp_timthumbs.size} timthumb file/s:")
				findings = wp_timthumbs.output(wpscan_options.verbose)
				collect_findings(findings)
			else
				puts info('No timthumb files found')
			end
		end
		# If we haven't been supplied a username/usernames list, enumerate them...
		if !wpscan_options.username && !wpscan_options.usernames && wpscan_options.wordlist || wpscan_options.enumerate_usernames
			puts
			puts info('Enumerating usernames ...')

			if wp_target.has_plugin?('stop-user-enumeration')
				puts warning("Stop User Enumeration plugin detected, results might be empty. However a bypass exists for v1.2.8 and below, see stop_user_enumeration_bypass.rb in #{__dir__}")
			end

			wp_users = WpUsers.aggressive_detection(wp_target,
								enum_options.merge(
									range: wpscan_options.enumerate_usernames_range,
									show_progression: false
			)
							       )

			if wp_users.empty?
				puts info('We did not enumerate any usernames')

				if wpscan_options.wordlist
					puts 'Try supplying your own username with the --username option'
					puts
					exit(1)
				end
			else
				puts info("Identified the following #{wp_users.size} user/s:")
				wp_users.output(margin_left: ' ' * 4)
				if wp_users[0].login == "admin"
					puts warning("Default first WordPress username 'admin' is still used")
				end
			end

		else
			wp_users = WpUsers.new

			if wpscan_options.usernames
				File.open(wpscan_options.usernames).each do |username|
					wp_users << WpUser.new(wp_target.uri, login: username.chomp)
				end
			else
				wp_users << WpUser.new(wp_target.uri, login: wpscan_options.username)
			end
		end
	rescue Exception=>e
		@scan_results[:status] = "Failed"
		@scan_results[:message] = e.message
	ensure
		@scan_results[:status] = "Success" unless @scan_results[:status].nil? && @scan_results[:status]!="Failed"
		result_file = File.open(self.out_put_file,"a+")
		result_file.puts @scan_results.to_json
		result_file.close
	end
	def collect_findings(findings)
		if !findings.nil? && findings.first.class.to_s == "Vulnerability"
			findings.each do |find|
				f = {title: find.title,severity: "High",externalLink: find.references, solution: find.fixed_in }
				@scan_results[:findings].push(f)
			end
		end
	end
end
wp_scan = WpScan.new
wp_scan.target = ARGV[0]
wp_scan.out_put_file = ARGV[1]
wp_scan.scan
