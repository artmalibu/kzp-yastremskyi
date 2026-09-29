# Правила проєкту КЗП
- Мова коду та стандартних термінів: Java 21+.
- Не конкатенуй рядки через operator +; використовуй String.formatted(), String.format() або printf.
- Числа форматуй із Locale.ROOT; шляхи будуй через Path.of().
- Кодування завжди StandardCharsets.UTF-8.
- Тести тільки JUnit 5 у src/test/java.