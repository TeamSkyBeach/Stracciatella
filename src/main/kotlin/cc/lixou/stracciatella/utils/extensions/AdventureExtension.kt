package cc.lixou.stracciatella.utils.extensions

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

fun String.formatMini(tagResolver: TagResolver = TagResolver.empty()): Component = MiniMessage.miniMessage().deserialize(this, tagResolver)

fun Component.undecorate(decoration: TextDecoration): Component = this.decoration(decoration, TextDecoration.State.FALSE)
fun Component.undecorate(vararg decorations: TextDecoration): Component = this.decorations(decorations.associateWith { TextDecoration.State.FALSE })

fun Component.italic(): Component = this.decorate(TextDecoration.ITALIC)
fun Component.strikethrough(): Component = this.decorate(TextDecoration.STRIKETHROUGH)
fun Component.bold(): Component = this.decorate(TextDecoration.BOLD)
fun Component.obfuscated(): Component = this.decorate(TextDecoration.OBFUSCATED)
fun Component.underlined(): Component = this.decorate(TextDecoration.UNDERLINED)

fun Component.noItalic(): Component = this.undecorate(TextDecoration.ITALIC)
fun Component.noStrikethrough(): Component = this.undecorate(TextDecoration.STRIKETHROUGH)
fun Component.noBold(): Component = this.undecorate(TextDecoration.BOLD)
fun Component.noObfuscated(): Component = this.undecorate(TextDecoration.OBFUSCATED)
fun Component.noUnderlined(): Component = this.undecorate(TextDecoration.UNDERLINED)