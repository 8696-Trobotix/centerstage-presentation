from dataclasses import dataclass

from manim import *
from manim_slides import Slide

# import mods.code_mobject

animation_buffer = []

class Wipe2(AnimationGroup):
    def __init__(self, mobject, target_mobject, shift=LEFT):
        self._mobject = mobject
        self._target_mobject = target_mobject
        super().__init__(FadeOut(mobject, shift=shift), FadeIn(target_mobject, shift=shift))
    
    def clean_up_from_scene(self, scene):
        super().clean_up_from_scene(scene)
        self._mobject.become(self._target_mobject)
        scene.add(self._mobject)
        scene.remove(self._target_mobject)

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

def focus_line(code: Code, line: int, target_y: float, add_to_buffer=False):
    global animation_buffer
    vgroup = code.line_numbers.chars[line - 1]
    dy = target_y - vgroup.get_y()
    anim = code.animate.shift(dy * UP)
    if add_to_buffer:
        animation_buffer.append(anim)
    return anim

def create_code_dialog(scene: Slide, code: Code, line: int, end_position: float, padding: float, text_width: float, text: str, add_to_buffer=False):
    global animation_buffer
    vgroup = code.code.chars[line - 1]
    x = vgroup.get_x()
    y = vgroup.get_y()
    indicator = Line(start=[end_position, y, 0], end=[x + vgroup.width / 2, y, 0])
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

def highlight_line(code: Code, line_num: int, color=PURE_GREEN):
    global hl_surrounding
    r = SurroundingRectangle(code.code.chars[line_num - 1], color=color, buff=0, stroke_width=2)
    hl_surrounding[line_num] = r
    return Create(r)

def unhighlight_line(code: Code, line_num: int):
    global hl_surrounding
    return Uncreate(hl_surrounding.pop(line_num))

def get_source_code(file_name: str, title: Title, lang):
    return code_mobject.Code(
        f"assets/source_code/{file_name}", 
        tab_width=4, 
        background="window", 
        language=lang, 
        style="github-dark"
    ).scale(1.4 * 0.405).next_to(title, DOWN).to_edge(LEFT)

@dataclass
class Step:
    title: any=None
    source_code: any=None
    section: any=None
    lang: any=None
    focus_line: any=None
    main_lines: any=None
    text: any=None
    add_load_anims: any=None
    add_unload_anims: any=None

def exposition(scene: Slide, steps: list[Step]):
    start = True
    load_anims = []

    current = Step()

    for step in steps:
        if start:
            current.title = Title(step.title)
            current.source_code = get_source_code(step.source_code, current.title, step.lang)
            # set_code_fade(current.source_code, 2.8)
            current.main_lines = BackgroundRectangle(
                VGroup(current.source_code.code.chars[step.main_lines[0]:step.main_lines[1] + 1]), 
                fill_color=YELLOW, 
                fill_opacity=0.5, 
                corner_radius=0.2
            )
            current.text = Paragraph(step.text, width=7).move_to(6 * RIGHT)

            scene.play(
                FadeIn(current.title), 
                FadeIn(current.source_code), 
                focus_line(current.source_code, step.focus_line, 2.5), 
                FadeIn(current.main_lines), 
                FadeIn(current.text)
            )

            start = False
