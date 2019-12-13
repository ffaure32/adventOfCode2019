package org.example

import java.awt.EventQueue
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.JFrame

fun main(args: Array<String>) {
    EventQueue.invokeLater(::createAndShowGUI)
}

private fun createAndShowGUI() {

    val frame = KotlinSwingSimpleEx("Simple")
    frame.isVisible = true
}

class KotlinSwingSimpleEx(s: String) : JFrame() {
    init {
        createUI(title)
    }

    private fun createUI(title: String) {

        setTitle(title)

        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        setSize(300, 200)
        setLocationRelativeTo(null)

        addKeyListener(object : KeyAdapter() {
            override fun keyPressed(e: KeyEvent) {
                if(e.keyCode == KeyEvent.VK_ENTER) {
                    isVisible = false
                    dispose()
                    System.exit(0)
                } else {
                    println(e.keyCode)
                }
            }
        })
        addPropertyChangeListener("test") {

        }
    }
}
