package top.e404.eapi.command

import top.e404.eapi.PL
import top.e404.eplugin.command.ECommandManager

object Commands : ECommandManager(
    PL,
    "eapiv",
    emptyList(),
    Debug,
    Reload,
)
