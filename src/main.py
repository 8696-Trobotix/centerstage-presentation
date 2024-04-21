from manim import *

from manim_slides import Slide
from manim_slides.slide.animation import Wipe, Zoom

from util import *

class Introduction(Slide):
    def construct(self):
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
        title = Title("Contents")
        description = Group(
            Paragraph(
                "- Main Codebase Organization", 
                "- mollusc:", 
                "  - Version", 
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
