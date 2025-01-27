# Инструкция по запуску проекта "Employee Processor"

## Описание проекта
Программа "Employee Processor" предназначена для обработки данных о сотрудниках и менеджерах из текстового файла. Она выполняет сортировку сотрудников, группировку их по департаментам, а также выводит результаты в консоль или файл. Некорректные данные обрабатываются отдельно и выводятся в конце.

## Возможности:
1. Чтение данных из текстового файла.
2. Обработка и фильтрация некорректных данных (например, отрицательная зарплата).
3. Группировка сотрудников по департаментам.
4. Сортировка сотрудников по имени или зарплате.
5. Вывод результатов в консоль или файл.
6. Подсчет статистики по департаментам (средняя зарплата и количество сотрудников).

## Требования к системе:
- **Java**: Версия 17 или выше.
- **Maven**: Версия 3.6.0 или выше.

## Сборка проекта
1. Скачайте или клонируйте репозиторий проекта.
   ```bash
   git clone <ссылка на репозиторий>
   ```
2. Перейдите в папку проекта:
   ```bash
   cd employee-processor
   ```
3. Соберите проект с помощью Maven:
   ```bash
   mvn clean package
   ```
4. После успешной сборки, JAR-файл будет находиться в папке `target`:
   ```plaintext
   target/employee-processor-1.0-SNAPSHOT.jar
   ```

## Формат входного файла
Входной файл должен быть в текстовом формате. Пример данных:
```plaintext
Manager,1,Jane Smith,5000,HR
Employee,101,John Doe,3000,1
Employee,102,Emily Johnson,3200,1
Manager,2,Michael Brown,6000,Sales
Employee,103,Chris White,2900,2
Employee,104,Anna Taylor,3100,2
Employee,105,Robert Black,-1000,2
```

### Формат строки:
```
<Должность>,<Идентификатор>,<Имя>,<Зарплата>,<Департамент/Идентификатор менеджера>
```
- **Должность**: `Manager` или `Employee`.
- **Идентификатор**: уникальное число.
- **Имя**: имя сотрудника.
- **Зарплата**: вещественное число больше 0.
- **Департамент/Идентификатор менеджера**: название департамента для менеджера или ID менеджера для сотрудника.

Некорректные строки (например, с отрицательной зарплатой или некорректными ID) будут выведены в разделе "Некорректные данные".

## Запуск программы

### Синтаксис команды:
```bash
java -jar target/employee-processor-1.0-SNAPSHOT.jar [аргументы]
```

### Доступные аргументы:
- `--sort=<name|salary>`: Критерий сортировки (по имени или зарплате). Если не указан, данные выводятся в исходном порядке.
- `--order=<asc|desc>`: Порядок сортировки:
    - `asc`: По возрастанию (по умолчанию).
    - `desc`: По убыванию.
- `--output=<console|file>`: Куда выводить результат:
    - `console`: Вывод в консоль (по умолчанию).
    - `file`: Вывод в файл.
- `--path=<путь>`: Путь к выходному файлу (обязателен, если `--output=file`).

### Примеры запуска:

#### Вывод результатов в консоль (по умолчанию):
```bash
java -jar target/employee-processor-1.0-SNAPSHOT.jar --sort=name --order=asc
```

#### Вывод результатов в файл:
```bash
java -jar target/employee-processor-1.0-SNAPSHOT.jar --output=file --path=output.txt --sort=salary --order=desc
```

#### Без сортировки (исходный порядок данных):
```bash
java -jar target/employee-processor-1.0-SNAPSHOT.jar --output=console
```

## Результат работы
### Формат вывода:

#### Пример вывода для департаментов:
```plaintext
HR
Manager,1,Jane Smith,5000.0
Employee,101,John Doe,3000.0
Employee,102,Emily Johnson,3200.0
amount,salary 2,3100.0

Sales
Manager,2,Michael Brown,6000.0
Employee,103,Chris White,2900.0
Employee,104,Anna Taylor,3100.0
amount,salary 2,3000.0
```

#### Пример вывода некорректных данных:
```plaintext
Некорректные данные:
Employee,105,Robert Black,-1000,2
```

## Возможные ошибки и их обработка
1. **Отсутствие входного файла:**
    - Сообщение: `Ошибка: Не указан путь к входному файлу.`
    - Решение: Укажите файл с данными.

2. **Некорректные аргументы командной строки:**
    - Сообщение: `Ошибка обработки аргументов: Некорректное значение для --sort.`
    - Решение: Проверьте синтаксис аргументов и используйте только допустимые значения.

3. **Некорректные строки данных:**
    - Некорректные строки будут добавлены в раздел "Некорректные данные" и не повлияют на обработку остальных данных.

## Поддержка и обратная связь
Если у вас возникли вопросы или проблемы с использованием программы, свяжитесь с разработчиком через [GitHub Issues].

"# employeeprocessor" 
"# employeeprocessor"  
