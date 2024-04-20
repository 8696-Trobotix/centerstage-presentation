from manim import *
from manim_slides import Slide

animation_buffer = []

def play_animation_buffer(scene: Slide):
    global animation_buffer
    scene.play(*animation_buffer)
    animation_buffer.clear()

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

def focus_line(scene: Slide, code: Code, line: int, target_y: float, add_to_buffer=False):
    global animation_buffer
    vgroup = code.line_numbers.chars[line - 1]
    dy = target_y - vgroup.get_y()
    anim = code.animate.shift(dy * UP)
    if add_to_buffer:
        animation_buffer.append(anim)
    else:
        scene.play(anim)

def create_code_dialog(scene: Slide, code: Code, line: int, end_position: float, padding: float, text_width: float, text: str, add_to_buffer=False):
    global animation_buffer
    vgroup = code.code.chars[line - 1]
    x = vgroup.get_x()
    y = vgroup.get_y()
    indicator = Line(start=[x + vgroup.width / 2, y, 0], end=[end_position, y, 0])
    # indicator = Line([0, 0, 0], end=[end_position, 0, 0])
    description = Paragraph(text)
    description.width = text_width
    border = SurroundingRectangle(description, buff=padding, color=WHITE)

    # indicator.next_to(vgroup, RIGHT)
    description.next_to(indicator, RIGHT)
    border.move_to(description)

    anims = (Create(indicator), Write(description), Create(border))
    if add_to_buffer:
        animation_buffer.extend(anims)
    else:
        scene.play(*anims)
    return indicator, description, border

def remove_code_dialog(scene: Slide, dialog, add_to_buffer=False):
    global animation_buffer

    anims = (Uncreate(dialog[0]), FadeOut(dialog[1]), Uncreate(dialog[2]))
    if add_to_buffer:
        animation_buffer.extend(anims)
    else:
        scene.play(*anims)

hl_surrounding = dict()

def highlight_line(code: Code, line_num: int):
    global hl_surrounding
    r = SurroundingRectangle(code.code.chars[line_num - 1], color=PURE_GREEN, buff=0)
    hl_surrounding[line_num] = r
    return Create(r)

def unhighlight_line(code: Code, line_num: int):
    global hl_surrounding
    return Uncreate(hl_surrounding.pop(line_num))
