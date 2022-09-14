package com.example.topmovies.fragment

interface ToolbarBridge : ToolbarBehaviour

interface ToolbarBehaviour {
    
    fun showBackButton()
    fun hideBackButton()
}
