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
                "- Main codebase organization.", 
                "- mollusc:", 
                "  - Version", 
                "  - Features", 
                "  - Organization", 
                "- Initialization", 
                "- Autonomous", 
                "- TeleOp", 
                line_spacing=0.5
            ).scale(0.75), 
            Text("Full source code is available in the team GitHub repository and at \nhttps://8696-trobotix.github.io/CENTERSTAGE.")
                .move_to(3 * DOWN)
                .scale(0.5)
        )

        self.play(FadeIn(title), FadeIn(description))

        self.next_slide(auto_next=True)
        fade_out_all(self)

class MainOrganization(Slide):
    def construct(self):
        title = Title("Main Codebase Organization")
        tree = Code(
            "assets/centerstage_tree.txt", 
            tab_width=4
        ).next_to(title, DOWN)
        note = Text("Further descriptions are available in the repository release section.")

        title.set_z_index(100)
        note.scale(0.5).to_edge(DOWN)

        self.play(FadeIn(title), FadeIn(tree))
        self.play(tree.animate.scale(0.425).next_to(title, DOWN), Write(note))

        self.next_slide()

        self.play(tree.animate.scale(1.4).next_to(title, DOWN).to_edge(LEFT), tree.background_mobject.animate.set_opacity(0), FadeOut(note))
        set_code_fade(tree, 2.8)

        data = [
            (
                17, 
                "alpha, beta, and gamma are old codebases.\nYou can ignore them.", 
                lambda : [], 
                lambda : []
            ), 
            (
                20, 
                "delta is the most up-to-date version.\nBe sure to reference the dev2 branch instead of the main branch\n(see the note in README on the main branch).", 
                lambda : [], 
                lambda : []
            ), 
            (12, 
                "mollusc library.", 
                lambda : [], 
                lambda : []
            ), 
            (
                29, 
                """Autonomous
- Initialization and drive functionality through mollusc.
- Hardware set up through SquidWare.
- Uses TotemPipeline as the image processing pipeline for detecting the team prop.
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
