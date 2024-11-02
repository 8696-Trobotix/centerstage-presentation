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
        self.wait(0.5)

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
        self.wait(0.5)

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

        self.play(Wipe2(note, Text("""Important: The main branch on the repository contains a version 
of the codebase integrated with a untested version of mollusc.
See the README section for more details.""").scale(0.4).to_edge(DOWN)))

        self.next_slide()

        self.play(tree.animate.scale(1.4).next_to(title, DOWN).to_edge(LEFT), tree.background_mobject.animate.set_opacity(0), FadeOut(note))
        set_code_fade(tree, 2.8)

        data = [
            (12, 
                """mollusc Library
Contains functionality that can be reused across OpModes.""", 
                lambda : [Wipe2(title, Title("Organization | mollusc"))], 
                lambda : []
            ), 
            (
                17, 
                """`alpha`, `beta`, and `gamma` contain old codebases.
They also contain older versions of mollusc.
Ignore these.""", 
                lambda : [Wipe2(title, Title("Organization | Directories"))], 
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
                    Wipe2(title, Title("Organization | Autonomous")), 
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
                    Wipe2(title, Title("Organization | TeleOp")), 
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
                lambda : [Wipe2(title, Title("Organization | Additional Assets"))], 
                lambda : []
            ), 
            (
                13, 
                """These scripts were used to quickly archive directories.
Ignore these.""", 
                lambda : [Wipe2(title, Title("Organization | Miscellaneous"))], 
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
            focus_line(tree, line_num, 0, add_to_buffer=True)
            play_animation_buffer(self)

            dialog = create_code_dialog(self, tree, line_num, -1, 0.1, 7, text, add_to_buffer=True)
            animation_buffer.extend(extra_anims())
            play_animation_buffer(self)

            self.next_slide()

            remove_code_dialog(self, dialog, add_to_buffer=True)
            animation_buffer.extend(unload_extra_anims())
        play_animation_buffer(self)
        self.wait(0.5)

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
        self.play(Wipe2(version, features_1))
        self.next_slide()
        self.play(Wipe2(version, features_2))
        self.next_slide()

        tree = Code(
            "assets/mollusc_v0.1.0_tree.txt", 
            tab_width=4
        ).next_to(title, DOWN)

        self.play(Wipe2(title, Title("mollusc | Organization")), FadeIn(tree), FadeOut(version))
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
                2, 
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
            focus_line(tree, line_num, 0, add_to_buffer=True)
            play_animation_buffer(self)

            dialog = create_code_dialog(self, tree, line_num, ls[0], 0.1, ls[1], text, add_to_buffer=True)
            animation_buffer.extend(extra_anims())
            play_animation_buffer(self)

            self.next_slide()

            remove_code_dialog(self, dialog, add_to_buffer=True)
            animation_buffer.extend(unload_extra_anims())
        play_animation_buffer(self)
        self.wait(0.5)

class Initialization(Slide):
    def construct(self):
        self.wait_time_between_slides = 0.1

        # Code("assets/source_code/what.txt", font="Fira Code")
        # Internal Manim bug halts progress here: https://github.com/ManimCommunity/manim/issues/3237

        exposition(self, [
            Step(
                title="Hardware Initialization", 
                source_code="SquidWare.java", 
                lang="java", 
                focus_line=19, 
                main_lines=(19, 19), 
                text="""All hardware initialization is done in `SquidWare` and shared between the autonomous and TeleOp."""
            ), 
            Step(
                focus_line=1, 
                main_lines=(1, 1), 
                text="""You can determine the directory of a class based on what package it's in. In this case, `SquidWare` is located under `TeamCode/src/main/java/org/firstinspires/ftc/teamcode/squid/delta`."""
            ), 
            Step(
                main_lines=(3, 3), 
                text="""Importing subsystem classes. These include the classes that manage the intake, launcher, scoring system, etc."""
            ), 
            Step(
                main_lines=(4, 8), 
                text="""Before we use mollusc functionality, we have to import the correct classes. The asterisk indicates that we're importing all available classes from that package."""
            ), 
            Step(
                main_lines=(11, 15), 
                text="""Import hardware device classes from the FTC SDK."""
            ), 
            Step(
                main_lines=(17, 17), 
                text="""Some packages will be located elsewhere."""
            ), 
            Step(
                focus_line=19, 
                main_lines=(21, 26), 
                text="""We define uninitialized objects using classes from mollusc for configuration, the drivetrain, and odometry pods. These will be initialized later. Defining these within the class scope allows us to access them from class methods."""
            ), 
            Step(
                main_lines=(28, 32), 
                text="""Here, we're initializing the subsystem objects directly within the class scope since we want to access attributes immediately."""
            ), 
            Step(
                section=(36, 110), 
                focus_line=1, 
                main_lines=(36, 36), 
                text="""The constructor is responsible for the bulk of hardware initialization. A `SquidWare` object is instantiated per the autonomous and TeleOp, which passes an already created configuration object to the constructor."""
            ), 
            Step(
                main_lines=(36, 36), 
                text="""This constructor must be marked as potentially thowing exceptions because `config` may be asked to obtain a configuration value that doesn't exist or is invalid."""
            ), 
            Step(
                main_lines=(39, 62), 
                text="""Each attribute in the intake subsystem (initialized earlier) is set using a configuration value obtained from the `config` object. If the value is not present or invalid, the entire constructor will cease execution and throw an exception to be handled by the autonomous / TeleOp."""
            ), 
            Step(
                focus_line=64, 
                main_lines=(64, 64), 
                text="""The `Make` class is provided by mollusc to make the process of initializing common hardware components (e.g. motors and servos) easier. With it, one can initialize a motor and set its direction without needing to specify separate method calls."""
            ), 
            Step(
                main_lines=(66, 71), 
                text="""`make.imu` is a method called from the `make` object which returns an initialized IMU with a specified name and orientation."""
            ), 
            Step(
                main_lines=(73, 78), 
                text="""Here, we're initializing base as a new four wheel drivetrain base. We use the `make` object again to create each of the four base motors. `getStringConfigDirection` is a method that takes a string ("forward" or "reverse") and returns the proper enum types."""
            ), 
            Step(
                focus_line=112, 
                text="""This is the `getStringConfigDirectionMethod`."""
            ), 
            Step(
                focus_line=80, 
                main_lines=(80, 98), 
                text="""Continue initializing trivial hardware."""
            ), 
            Step(
                main_lines=(99, 106), 
                text="""An `Encoder` object is really just a motor, but additional methods have been provided to get the encoder ticks / counts, revolutions, and displacement. "TPR" is short for "ticks per revolution". When instantiating a new `Encoder`, you can provide either a string name, existing motor, followed by a multiplier (a negative multiplier effectively reverses the encoder direction), the TPR, and finally the wheel diameter (if applicable)."""
            ), 
            Step(
                main_lines=(102, 106), 
                text="""Initializing the odometry pod object respondible for calculating the robot's coordinates. In the future, this team may want to invest in learning Road Runner, a library that provides more advanced and accurate odometry control. The process is more lengthy, but could potentially result in more accurate robot movements, especially as the robot's mass increases or if a constant velocity is absolutely required. The `DeadWheels` object is instantiated with an initial orientation of 180 degrees, or a pose of (0, 0, PI radians). Each of the odometry pod encoders are supplied and we provide the track width (distance between two parallel encoders) and the center offset (distance between the perpendicular encoder and the robot's center of rotation). The units of these values are up to the user, so long as they're consistent with the way the encoders were initialized and the user keeps them in mind. Here, we're using millimeters."""
            )
        ])

import mods.code_mobject
class Test(Slide):
    def construct(self):
        for style in Code.styles_list:
            thing = mods.code_mobject.Code(
                "assets/source_code/SquidWare.java", 
                tab_width=4, 
                background="window", 
                language="java", 
                style=style
            ).scale(0.5)

            self.add(thing)
            self.play(thing.animate.shift(DOWN * 15))
            self.play(thing.animate.shift(UP * 15))
            self.wait(1)

            break
