1. список валют в файле config.xml. Если файла нет, то будет сообщение об ошибке и выход
2. если не найден файл инициализации, то будет сообщение об этом, но можно будет продолжить работу. Неверные строки с валютой и суммой игнорируются
3. если в консоли неверно набрано название валюты, то будет сообщение об ошибке и выход. Неверный ввод суммы игнорируется
4. все эксепшны перехватываются и выводятся в консоль с тектом "Ошибка" и стэктрейсом.