package com.example.moremenu.model

interface MoreMenuSideEffect {
    data class NavigateMenu(val menu: Menu) : MoreMenuSideEffect
}
