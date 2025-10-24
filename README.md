# OXONO – Strategy Game in JavaFX

> A reproduction of the **OXONO** board game developed in **Java** with **JavaFX**.  
> This project demonstrates key concepts in **Object-Oriented Programming**, **MVC architecture**, and **modern user interface design** using JavaFX.

---

## About the Project
The goal of this project is to develop a complete Java application — from game logic to graphical interface — while applying solid software engineering principles.  
**OXONO** is a turn-based strategy game where two players compete to align their symbols on a grid.

---

## Main Features
- Graphical interface built with **JavaFX**  
- Complete game logic (rules, turns, victory conditions)  
- **Player vs Player** and **Player vs AI** modes  
- **Undo / Redo** system using the Command pattern  
- Clear and modular **MVC architecture** 
- **Observer design pattern**  to keep the view updated throughout the game

---

## Preview

![Undo Redo Demo](oxono-img/undo_redo.gif)
---

## Installation & Execution
1. Clone the repository:
   ```bash
   git clone https://github.com/<your-username>/oxono.git
   cd oxono
   mvn clean javafx:run
