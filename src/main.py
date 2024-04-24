from manim import *

from manim_slides import Slide
from manim_slides.slide.animation import Wipe, Zoom

from util import *

class Introduction(Slide):
    def construct(self):
        self.wait_time_between_slides = 0.1
        title = Title("Trobotix CENTERSTAGE Source Code Overview")
        centerstage = ImageMobject("assets/centerstage.png")
        icon = ImageMobject("assets/icon.png")
        robot = ImageMobject("assets/robot.jpg")

        width = 8
        centerstage.width = width
        icon.width = width / 2
        robot.width = width / 2

        subtitle = Group(
            centerstage, 
            Group(
                icon, 
                robot.next_to(icon, RIGHT)
            ).next_to(centerstage, DOWN)
        ).next_to(title, DOWN)

        self.add(title, subtitle)
        self.wait()

        self.next_slide(auto_next=True)
        fade_out_all(self)

class Contents(Slide):
    def construct(self):
        self.wait_time_between_slides = 0.1
        title = Title("Contents")
        description = Group(
            Paragraph(
                "- Main Codebase Organization", 
                "- mollusc:", 
                "  - Versions", 
                "  - Features", 
                "  - Organization", 
                "- Exposition:", 
                "  - Initialization", 
                "  - Autonomous", 
                "  - TeleOp", 
                line_spacing=0.5
            ).scale(0.75), 
            Text("Full source code is available on the team GitHub organization and the programming website.")
                .move_to(3 * DOWN)
                .scale(0.5)
        )

        self.play(FadeIn(title), FadeIn(description))

        self.next_slide(auto_next=True)
        fade_out_all(self)

class MainOrganization(Slide):
    def construct(self):
        self.wait_time_between_slides = 0.1
        title = Title("Main Codebase Organization", z_index=100)
        tree = Code(
            "assets/centerstage_tree.txt", 
            tab_width=4
        ).next_to(title, DOWN)
        note = Text("""Further descriptions are available in the 
`centerstage` repository README and release sections.""")

        note.scale(0.5).to_edge(DOWN)

        self.play(FadeIn(title), FadeIn(tree))
        self.play(tree.animate.scale(0.405).next_to(title, DOWN), Write(note))

        self.next_slide()

        self.play(Transform(note, Text("""Important: The main branch on the repository contains a version 
of the codebase integrated with a untested version of mollusc.
See the README section for more details.""").scale(0.4).to_edge(DOWN)))

        self.next_slide()

        self.play(tree.animate.scale(1.4).next_to(title, DOWN).to_edge(LEFT), tree.background_mobject.animate.set_opacity(0), FadeOut(note))
        set_code_fade(tree, 2.8)

        data = [
            (12, 
                """mollusc Library
Contains functionality that can be reused across OpModes.""", 
                lambda : [Transform(title, Title("Organization | mollusc"))], 
                lambda : []
            ), 
            (
                17, 
                """`alpha`, `beta`, and `gamma` contain old codebases.
They also contain older versions of mollusc.
Ignore these.""", 
                lambda : [Transform(title, Title("Organization | Directories"))], 
                lambda : []
            ), 
            (
                20, 
                """`delta` contains the most up-to-date source code.
Always reference this directory instead of the older ones.""", 
                lambda : [], 
                lambda : []
            ), 
            (
                29, 
                """Autonomous
- Initialization and drive functionality through mollusc.
- Hardware set up through `SquidWare`.
- Uses `TotemPipeline` as the image processing pipeline for detecting the team prop.
- May use various subsystems to score.""", 
                lambda : [
                    Transform(title, Title("Organization | Autonomous")), 
                    highlight_line(tree, 22), 
                    highlight_line(tree, 23), 
                    highlight_line(tree, 31)
                ], 
                lambda : [
                    unhighlight_line(tree, 22), 
                    unhighlight_line(tree, 23), 
                    unhighlight_line(tree, 31)
                ]
            ), 
            (
                30, 
                """TeleOp
- Initialization and drive functionality also through mollusc.
- Hardware set up shared with Autonomous in `SquidWare`.
- Uses subsystems.""", 
                lambda : [
                    Transform(title, Title("Organization | TeleOp")), 
                    highlight_line(tree, 23), 
                    highlight_line(tree, 31)
                ], 
                lambda : [
                    unhighlight_line(tree, 23), 
                    unhighlight_line(tree, 31)
                ]
            ), 
            (
                5, 
                """Additional Assets
- `SquidWare` (hardware initialization) uses mollusc 
  to parse constants and other variables from `delta.txt` (i.e. PID coefficients).
- `script_delta.txt` contains the full autonomous script 
  read by `AutoSquidDelta`.""", 
                lambda : [Transform(title, Title("Organization | Additional Assets"))], 
                lambda : []
            ), 
            (
                13, 
                """These scripts were used to quickly archive directories.
Ignore these.""", 
                lambda : [Transform(title, Title("Organization | Miscellaneous"))], 
                lambda : []
            ), 
            (
                36, 
                """These scripts can be used to help make changes remotely.
- `pull.sh` and `push.sh` are shortcuts to pull / push to GitHub (e.g. upload / download).
- `atlas.yml` and `summon.py` are used to establish a remote session 
  whereby a programmer can edit code on the ThinkPad remotely.""", 
                lambda : [
                    highlight_line(tree, 35), 
                    highlight_line(tree, 36), 
                    highlight_line(tree, 37), 
                    highlight_line(tree, 38)
                ], 
                lambda : [
                    unhighlight_line(tree, 35), 
                    unhighlight_line(tree, 36), 
                    unhighlight_line(tree, 37), 
                    unhighlight_line(tree, 38)
                ]
            )
        ]

        for line_num, text, extra_anims, unload_extra_anims in data:
            focus_line(self, tree, line_num, 0, add_to_buffer=True)
            play_animation_buffer(self)

            dialog = create_code_dialog(self, tree, line_num, -1, 0.1, 7, text, add_to_buffer=True)
            animation_buffer.extend(extra_anims())
            play_animation_buffer(self)

            self.next_slide()

            remove_code_dialog(self, dialog, add_to_buffer=True)
            animation_buffer.extend(unload_extra_anims())
        play_animation_buffer(self)

class MolluscDetails(Slide):
    def construct(self):
        self.wait_time_between_slides = 0.1
        title = Title("mollusc")
        version = MarkupText("""<big>Versions</big>

There are three "versions" of mollusc:
- <b>v0.1.0</b> is the "stable" version that should be used.
- The <b>main branch</b> on the mollusc repository contains a refactored version of <b>v0.1.0</b> with an improved interface and additional features. However, it has not been tested and should not be used unless it is tested in the future.
- <b>6e446fa</b> is the commit hash / ID of the revision of mollusc used at state. It's practically identical to <b>v0.1.0</b> and can be treated as such.

Currently, mollusc can be added to any project by downloading or cloning it into the `teamcode` folder.
A more convenient method may be developed in the future.""")
        features_1 = MarkupText("""<big>Tested Features</big>
- An "intepreter" that reads, parses, and executes a file containing a primitive yet customizable command-like language to control autonomous movements.
Three dead wheel system odometry calculations.
Note:
From an initial starting orientation, forward movement corresponds to a positive X translation and rightward movement corresponds to a positive Y translation.
<tt>
           /\\
           | +x
 -y   robot front   +y
&lt;-----           -----&gt;
      robot back
           | -x
           \\/
</tt>
- Mecanum drive controlled by dead wheels for autonomous.
- Field centric and robot centric TeleOp drivetrains.
- External asset loading.
- Program configuration via the Driver Station.
- Gamepad controls utilities.
- PIDF controller.
- Contour-based multiple object detector for CV pipelines.""")
        features_2 = MarkupText("""<big>Untested Features</big>
- Mecanum drive controlled by wheel encoders for autonomous.
- Some driver station configuration functions.
- Low-pass and Kalman filters.
- Threaded function execution wrapper.
- Voltage compensator.
- AprilTag detection EOCV pipeline
  (newer versions of the robot controller SDK have this built in).""")

        version.scale(0.5).next_to(title, DOWN)
        features_1.scale(0.32).next_to(title, DOWN)
        features_2.scale(0.32).next_to(title, DOWN)

        self.play(FadeIn(title), FadeIn(version))
        self.next_slide()
        self.play(Transform(version, features_1))
        self.next_slide()
        self.play(Transform(version, features_2))
        self.next_slide()

        tree = Code(
            "assets/mollusc_v0.1.0_tree.txt", 
            tab_width=4
        ).next_to(title, DOWN)

        self.play(Transform(title, Title("mollusc | Organization")), FadeIn(tree), FadeOut(version))
        self.play(tree.animate.scale(0.5).next_to(title, DOWN))

        self.next_slide()

        self.play(tree.animate.scale(1.4).next_to(title, DOWN).to_edge(LEFT), tree.background_mobject.animate.set_opacity(0))
        set_code_fade(tree, 2.8)

        def shift_dialog(ls, new_indicator_end, new_text_width):
            ls[0] = new_indicator_end
            ls[1] = new_text_width
            return []

        ls = [-1, 7]
        data = [
            (
                37, 
                """Provides a convenient way to access the 
main OpMode from multiple classes.""", 
                lambda : [], 
                lambda : []
            ), 
            (
                32, 
                """Wrappers that provide additional functionality to existing SDK components.
- `Encoder`: Reads the encoder counts and calculates the number 
  of revolutions / distance traveled from a single encoder port.
- `Make`: Provides shortcut functions to initialize hardware 
  (i.e. initialize a motor and set its direction).
- `MolluscLinearOpMode`: Wraps `LinearOpMode` so that `Mollusc` works.
- `MolluscOpMode`: Wraps `OpMode` for the same reason.""", 
                lambda : [], 
                lambda : [
                    *shift_dialog(ls, 0, 6)
                ]
            ), 
            (
                23, 
                """Configuration utilities, controls, and PIDF controller.
- `Asset`: Reads a text-based asset file separate from the source code.
- `Configuration`: Parses the asset and provides methods 
  for obtaining constants and other values specific in the asset.
- `Controls`: Enhances gamepad controls 
  (i.e. only perform an action when a button is pressed but not held).
- `PID`: PIDF controller. Also contains a function that 
  enables bulk reading (faster cycle times).""", 
                lambda : [], 
                lambda : [
                    *shift_dialog(ls, -1, 7)
                ]
            ), 
            (
                3, 
                """Autonomous-related classes.
Red: Deadwheel odometry.
Green: Script interpreter.
Blue: Autonomous base classes.""", 
                lambda : [
                    highlight_line(tree, 3, PURE_RED), 
                    highlight_line(tree, 4, PURE_RED), 
                    highlight_line(tree, 5, PURE_RED), 
                    highlight_line(tree, 6, PURE_GREEN), 
                    highlight_line(tree, 7, PURE_GREEN), 
                    highlight_line(tree, 8, PURE_GREEN), 
                    highlight_line(tree, 9, PURE_GREEN), 
                    highlight_line(tree, 10, PURE_BLUE), 
                    highlight_line(tree, 11, PURE_BLUE)
                ], 
                lambda : [
                    unhighlight_line(tree, 3), 
                    unhighlight_line(tree, 4), 
                    unhighlight_line(tree, 5), 
                    unhighlight_line(tree, 6), 
                    unhighlight_line(tree, 7), 
                    unhighlight_line(tree, 8), 
                    unhighlight_line(tree, 9), 
                    unhighlight_line(tree, 10), 
                    unhighlight_line(tree, 11)
                ]
            ), 
            (
                10, 
                """Use `MecanumAutoII` instead of `MecanumAutoI`.
II uses three deadwheel odometry, whereas I attempts to do localization 
based on wheel encoders and has not been tested.""", 
                lambda : [
                    highlight_line(tree, 10, PURE_GREEN), 
                    highlight_line(tree, 11, PURE_RED)
                ], 
                lambda : [
                    unhighlight_line(tree, 10), 
                    unhighlight_line(tree, 11), 
                    *shift_dialog(ls, -0.5, 6.5)
                ]
            ), 
            (
                12, 
                """Field-centric and robot-centric base classes.""", 
                lambda : [], 
                lambda : [
                    *shift_dialog(ls, -1, 7)
                ]
            ), 
            (
                28, 
                """Computer vision calculations.
Use `ObjectDetector` to detect `VisionObject`s given a set of `ColorRange`s.""", 
                lambda : [], 
                lambda : [
                    *shift_dialog(ls, 0, 6)
                ]
            ), 
            (
                17, 
                """Various methods (i.e. those in `Configuration`) 
may thow exceptions when a user-error is detected.
Example: Requesting a configuration value that doesn't exist.
Exceptions will normally terminate the OpMode 
unless caught and handled.

- `AssetRetrievalException`: The asset couldn't be loaded 
  (possibly misnamed or in the wrong place).
- `ConfigValueMissingException`: The configuration value doesn't exist.
- `ParityException`: A feature was used that works in `LinearOpMode`s 
  but not in `OpMode`s.
- `ScriptParseException`: An error was encountered 
  while parsing the autonomous script.""", 
                lambda : [], 
                lambda : [
                    *shift_dialog(ls, -1, 7)
                ]
            ), 
            (
                22, 
                """These are tests that were intended to verify the library's functionality.
However, the tests themselves were never tested or used, so they can be ignored.""", 
                lambda : [], 
                lambda : []
            )
        ]

        for line_num, text, extra_anims, unload_extra_anims in data:
            focus_line(self, tree, line_num, 0, add_to_buffer=True)
            play_animation_buffer(self)

            dialog = create_code_dialog(self, tree, line_num, ls[0], 0.1, ls[1], text, add_to_buffer=True)
            animation_buffer.extend(extra_anims())
            play_animation_buffer(self)

            self.next_slide()

            remove_code_dialog(self, dialog, add_to_buffer=True)
            animation_buffer.extend(unload_extra_anims())
        play_animation_buffer(self)
