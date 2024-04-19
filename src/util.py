from manim import *
from manim_slides import Slide

def fade_out_all(scene: Slide):
    scene.play(*[FadeOut(i) for i in scene.mobjects])

def set_code_fade(code: Code, max_y: float):
    def updater(m, dt):
        dy = max_y - m.get_y()
        if dy < 0:
            opacity = 0
        else:
            opacity = min(dy, 1)
        m.set_opacity(opacity)
    for line in code.line_numbers.chars:
        line.add_updater(updater)
    for line in code.code.chars:
        line.add_updater(updater)

def focus_line(scene: Slide, code: Code, line: int, target_y: float):
    vgroup = code.line_numbers.chars[line - 1]
    dy = target_y - vgroup.get_y()
    scene.play(code.animate.shift(dy * UP))

def create_code_dialog(scene: Slide, code: Code, line: int, indicator_length: float, padding: float, text_width: float, text: str):
    vgroup = code.code.chars[line - 1]    
    indicator = Line(start=[0, 0, 0], end=[indicator_length, 0, 0])
    description = Paragraph(text)
    border = Rectangle(width=text_width + 2 * padding, height=description.height + 2 * padding)

    description.width = text_width
    indicator.next_to(vgroup, RIGHT)
    description.next_to(indicator, RIGHT)
    description.shift(padding * RIGHT)
    border.move_to(description)

    scene.play(Create(indicator), Write(description), Create(border))
    return indicator, description, border

def remove_code_dialog(scene: Slide, dialog):
    scene.play(Uncreate(dialog[0]), Unwrite(dialog[1]), Uncreate(dialog[2]))
