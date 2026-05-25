extends Control

func _ready() -> void: 
	$statusbar/version.text = games[selection][1]
	$screenshot.texture = games[selection][2]
	
	get_window().title = "Lucas' Computer Game Collection Vol.1 - Launcher"
	
	$intro.visible = true
	var tween = get_tree().create_tween() \
		.set_ease(Tween.EASE_OUT) \
		.set_trans(Tween.TRANS_EXPO)
	
	var logo_y_position :float = $intro/doodles/logo.position.y
	$intro/doodles/logo.position.y -= 250
	tween.tween_property($intro/doodles/logo, "position:y", logo_y_position, 1.0)
	
	await tween.finished
	await get_tree().create_timer(1).timeout
	
	$intro/roll.play()
	tween = get_tree().create_tween() \
		.set_ease(Tween.EASE_IN) \
		.set_trans(Tween.TRANS_EXPO)
	tween.tween_property($intro/doodles, "position:y", -200, 0.5)
	
	await tween.finished
	tween = get_tree().create_tween() \
		.set_ease(Tween.EASE_OUT) \
		.set_trans(Tween.TRANS_EXPO)
	$intro.visible = false
	position.y = 150
	tween.tween_property(self, "position:y", 0, 0.5)

func _on_website_pressed() -> void: OS.shell_open("https://lucasangelo.dev")

@onready var games = [
	["nietzsche", "v2.3rev0", preload("uid://chgvefennm6p0"), $catalog/card_b, $selection/selection_b], # Blood Loss
	["wargames", "v2.3rev0", preload("uid://m7juhxk5e1ma"), $catalog/card_t, $selection/selection_t], # Tidal Hell
	["caledonia", "v2.2rev0", preload("uid://cy17pk1kvw7eb"), $catalog/card_c, $selection/selection_c], # Castration
	["amianto", "v1.3rev0", preload("uid://d26tdpn0fwbe3"), $catalog/card_f, $selection/selection_f], # Full Metal Syncope
	["pier", "v4.2rev0", preload("uid://ci2hnb50s5h7q"), $catalog/card_g, $selection/selection_g], # GIFT
	["yosemite", "v1.0rev0", preload("uid://c2lkmqrc5qven"), $catalog/card_n, $selection/selection_n]] # nDV: NeuroDive

func set_selection(value :int): selection = value
func increment_selection(): selection += 1
func decrement_selection(): selection -= 1

var _selection_tweem :Tween
const TWEEN_DURATION := .5
var selection :int = 0:
	set(value):
		if value < 0: value = games.size()-1
		if value > games.size()-1: value = 0
		
		$click.pitch_scale = randf_range(0.9, 1.2)
		$click.play()
		
		$statusbar/version.text = games[value][1]
		
		for entry in games: entry[3].visible = false
		for entry in games: entry[4].modulate = Color(1., 1., 1., 0.5)
		games[value][3].visible = true
		games[value][4].modulate = Color(1., 1., 1., 1)
		
		if selection != value:
			if _selection_tweem: _selection_tweem.kill()
			_selection_tweem = get_tree().create_tween() \
				.set_parallel() \
				.set_ease(Tween.EASE_OUT) \
				.set_trans(Tween.TRANS_EXPO)
			
			$screenshot.texture = games[value][2]
		
			$screenshot.modulate.a = 0.0
			$statusbar.modulate.a = 0.0
			_selection_tweem.tween_property($screenshot, "modulate:a", 0.5, TWEEN_DURATION)
			_selection_tweem.tween_property($statusbar, "modulate:a", 1.0, TWEEN_DURATION*3)
			
			$catalog.modulate.a = 0.0
			_selection_tweem.tween_property($catalog, "modulate:a", 1.0, TWEEN_DURATION)
			
			if value > selection: $catalog.position = Vector2(80, 0)
			elif value < selection: $catalog.position = Vector2(-80, 0)
			_selection_tweem.tween_property($catalog, "position", Vector2.ZERO, TWEEN_DURATION)
			_selection_tweem.tween_property($catalog, "position", Vector2.ZERO, TWEEN_DURATION)
		
		selection = value

var _music_last_playback_position :float = 0
func _play_music(): 
	if $music.playing: return
	$music.play(_music_last_playback_position)
func _stop_music():
	_music_last_playback_position = $music.get_playback_position()
	$music.stop()

var _last_played_game :String = ""
func _on_play_pressed() -> void:
	$statusbar/play/click.play()
	_stop_music()
	
	var game_file_name :String = games[selection][0] + "-" + games[selection][1]
	
	var bin_dir :String = OS.get_executable_path().get_base_dir()
	var data_dir :String = bin_dir.path_join("data")
	
	var game_file_path :String = data_dir.path_join(game_file_name)
	if OS.has_feature("linux"): game_file_path += ".x86_64"
	elif OS.has_feature("windows"): game_file_path += ".exe"
	
	print(game_file_path)
	var output = []
	print(OS.execute(game_file_path, [], output))
	print(output)

func _notification(what: int) -> void:
	if what == NOTIFICATION_APPLICATION_FOCUS_IN: _play_music()
	elif what == NOTIFICATION_APPLICATION_FOCUS_OUT: _stop_music()

func _input(event: InputEvent) -> void:
	if event is InputEventMouseButton:
		if event.pressed:
			_play_music()
	if event is InputEventKey:
		if event.pressed:
			if event.keycode == KEY_ESCAPE:
				get_tree().quit()
			elif event.keycode == KEY_F4:
				if DisplayServer.window_get_mode() != DisplayServer.WINDOW_MODE_FULLSCREEN:
					DisplayServer.window_set_mode(DisplayServer.WINDOW_MODE_FULLSCREEN)
				else:
					DisplayServer.window_set_mode(DisplayServer.WINDOW_MODE_WINDOWED)
