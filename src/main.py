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

        title.set_z_index(100)

        self.play(FadeIn(title), FadeIn(tree))
        self.play(tree.animate.scale(0.5).next_to(title, DOWN))

        self.next_slide()

        self.play(tree.animate.scale(1.4).next_to(title, DOWN).to_edge(LEFT), tree.background_mobject.animate.set_opacity(0))
        set_code_fade(tree, 2.8)

        data = [
            (17, "alpha, beta, and gamma are old codebases.\nYou can ignore them.")
        ]

        for line_num, text in data:
            focus_line(self, tree, line_num, 0)
            dialog = create_code_dialog(self, tree, line_num, 1.5, 0.1, 5, text)

            self.next_slide()

            remove_code_dialog(self, dialog)
