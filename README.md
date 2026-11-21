# tank-software-design
## Архитектура игры

- **Domain** — игровые сущности и правила: `Tank`, `TankState`, `Player`, `Bullet`, `Tree`, `GameWorld`, столкновения и выбор стратегии бота. Через `World`, `WorldView` и `Obstacles` объекты получают только нужные операции мира. Через `WorldListener` мир сообщает о появлении и удалении объектов.
- **Application** — `InputHandler` и `AiController` выбирают действия на кадр. `MoveCommand` и `ShootCommand` вызывают операции модели. `LevelBuilder` собирает мир из описания уровня.
- **Infrastructure** — `GameDesktopLauncher` связывает компоненты с игровым циклом libGDX. `GdxKeyboardInput` читает клавиатуру, `LevelParserFromFile` читает файл, `TextureRepository` загружает текстуры. `GameRender`, `HealthBarRenderer` и `GroundLayer` отвечают за представление. `WorldObject` тоже относится к представлению: он связывает сущность с текстурой.

### Где границы ещё связаны с libGDX

`LevelBuilder` получает размеры объектов из текстур и регистрирует спрайты. Порт для размеров и отдельный адаптер регистрации спрайтов позволили бы собирать мир без графики. `ToggleHealthBarsCommand` напрямую зависит от `HealthBarRenderer`: ему достаточно порта с операцией переключения полосок. Модель и порты геометрии используют `GridPoint2` и `Rectangle` из libGDX. Собственные типы координат и преобразование в графическом адаптере изолировали бы эти зависимости.
