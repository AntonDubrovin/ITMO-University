package ru.itmo.web.lesson4.util;

import ru.itmo.web.lesson4.model.User;
import ru.itmo.web.lesson4.model.Post;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DataUtil {
    private static final List<User> USERS = Arrays.asList(
            new User(1, "MikeMirzayanov", "Mike Mirzayanov", Colors.BLACK),
            new User(6, "pashka", "Pavel Mavrin", Colors.RED),
            new User(7, "cannon147", "Erofey Bashunov", Colors.DARKCYAN),
            new User(9, "geranazarov555", "Georgiy Nazarov", Colors.BLUE),
            new User(11, "tourist", "Gennady Korotkevich", Colors.RED)
    );

    private static final List<Post> POSTS = Arrays.asList(
            new Post(1, "About the Failed Round 591/Technocup 2020 — Elimination Round 1", "Привет, Codeforces!" + "\n" +
                    "" + "\n" +
                    "К сожалению, недоброжелатели сорвали проведение раунда, сделав DDOS на нашу инфраструктуру. " +
                    "Ни координатор, ни авторы раунда не виноваты, что у вас не получилось полноценно принять участие в раунде. " +
                    "Пожалуйста, не минусуйте анонс раунда. Я думаю, что такая ситуация — дополнительный повод поддержать авторов. " +
                    "Они подготовили хорошие задачи!" + "\n" +
                    "" + "\n" +
                    "Видимо, подобную атаку надо расценивать как симптом того, что Codeforces перерос фазу юношества и " +
                    "вступил во взрослую серьезную жизнь. Конечно, мы ответим адекватными мерами, чтобы защититься от подобных инцидентов. " +
                    "К счастью, за почти 10 лет работы вокруг сложилось большое сообщество тех, кому небезразличен Codeforces. " +
                    "Мы не переживаем по поводу возможных дополнительных трат или приложенных усилий. У нас всё получится. " +
                    "Раунды должны продолжаться.", 1, 3234, 12, 170),
            new Post(2, "EDU: Segment Tree, part 1", "EDU: Segment Tree, part 1 Hello everyone! " +
                    "I just published a [new lesson](https://codeforces.com/edu/course/2/lesson/4) in the EDU section. " +
                    "This is the first part of the lesson about the segment tree." +
                    " <img src=\"/predownloaded/ad/f8/adf89a39c4a3c646e9b047bc1e24e894a01034d4.png\"/> " +
                    "In this lesson, we will learn how to build a simple segment tree (without mass modifications), " +
                    "and how to perform basic operations on it. We will also discuss some tasks that can be solved " +
                    "using the segment tree. <center style=\"margin:2.5em;\"> <a href=\"/edu/courses\" " +
                    "style=\"text-decoration:none; font-size:18px; background-color:#01579B; color:white; " +
                    "font-weight:bold; padding:0.5em 1em;\">Go to EDU &rarr;</a> </center> More about EDU section " +
                    "you can read in [this](/blog/entry/79530) post. Hope it will be helpful, enjoy!", 6, 1309, 3, 27),
            new Post(3, "Codeforces Global Round 5", "Всем привет!" + "\n" +
                    "" + "\n" +
                    "Скоро состоится Codeforces Global Round 5, время начала: среда, 16 октября 2019 г. в 17:35." + "\n" +
                    "" + "\n" +
                    "Это пятый раунд из серии Codeforces Global Rounds, которая проводится при поддержке XTX Markets. В раундах могут участвовать все, рейтинг тоже будет пересчитан для всех." + "\n" +
                    "" + "\n" +
                    "Соревнование продлится 2 часа 30 минут, вас ожидает 8 задач, при этом одна из задач будет представлена в двух версиях." + "\n" +
                    "" + "\n" +
                    "Разбалловка: 500 — 750 — (750 + 750) — 2000 — 2500 — 3000 — 3750 — 4000" + "\n" +
                    "" + "\n" +
                    "Призы в этом раунде:" + "\n" +
                    "" + "\n" +
                    "30 лучших участников получат футболки." + "\n" +
                    "20 футболок будут разыграны случайным образом среди участников с 31-го по 500-е место." + "\n" +
                    "Призы в серии из 6 раундов в 2019 году:" + "\n" +
                    "" + "\n" +
                    "За каждый раунд лучшим 100 участникам начисляются баллы согласно таблице." + "\n" +
                    "Итоговый результат участника равны сумме баллов для четырех лучших выступлений этого участника." + "\n" +
                    "Лучшие 20 участников по итоговым результатам получают толстовки и сертификаты с указанием места." + "\n" +
                    "Задачи раунда разработаны мной, а вот список людей, которым нельзя принимать участие в этом раунде, потому что они знали задачи заранее:" + "\n" +
                    "" + "\n" +
                    "KAN, Endagorion, arsijo, Rox, lperovskaya, Aleks5d, Learner99, MikeMirzayanov." + "\n" +
                    "" + "\n" +
                    "Так совпало, что это одновременно и те самые люди, которым я благодарен за то, что этот раунд такой, какой он есть." + "\n" +
                    "" + "\n" +
                    "Раунд будет идеально сбалансирован.", 11, 42, 2, 17),
            new Post(4, "ITMO Algorithms Course", "Hello Codeforces!" + "\n" +
                    "" + "\n" +
                    "I teach a course on algorithms and data structures at ITMO University. During the last year I was streaming all my lectures on Twitch and uploaded the videos on Youtube." + "\n" +
                    "" + "\n" +
                    "This year I want to try to do it in English." + "\n" +
                    "" + "\n" +
                    "This is a four-semester course. The rough plan for the first semester:" + "\n" +
                    "" + "\n" +
                    "Algorithms, complexity, asymptotics" + "\n" +
                    "Sorting algorithms" + "\n" +
                    "Binary heap" + "\n" +
                    "Binary search" + "\n" +
                    "Linked lists, Stack, Queue" + "\n" +
                    "Amortized analysis" + "\n" +
                    "Fibonacci Heap" + "\n" +
                    "Disjoint Set Union" + "\n" +
                    "Dynamic Programming" + "\n" +
                    "Hash Tables" + "\n" +
                    "The lectures are open for everybody. If you want to attend, please fill out this form to help me pick the optimal day and time." + "\n" +
                    "" + "\n" +
                    "See you!", 6, 879, 7, 88),
            new Post(5, "Разбор задач \"Осеннего программиста 2019\"", "Задача A. Отпуск" + "\n" +
                    "Если x+y<n, то было n−x−y дней без шторма и холодов, если же x+y≥n, значит не было ни одного такого дня." + "\n" +
                    "" + "\n" +
                    "Пример кода на языке Python:" + "\n" +
                    "" + "\n" +
                    "n = int(input())" + "\n" +
                    "x = int(input())" + "\n" +
                    "y = int(input())" + "\n" +
                    "if x + y < n:" + "\n" +
                    "    print(n - x - y)" + "\n" +
                    "else:" + "\n" +
                    "    print(0)" + "\n" +
                    "Задача B. Простая игра" + "\n" +
                    "Минимальное число ходов будет, если все время будет выпадать 6. В этом случае число ходов будет равно n/6, округленное вверх до ближайшего целого. В большинстве языков по умолчанию делается с округлением вниз, чтобы сделать округление вверх, проще всего использовать формулу (n+5)/6, при этом деление делается с округлением вниз. Максимальное число ходов будет очевидно равно n." + "\n" +
                    "" + "\n" +
                    "Пример кода на языке Python:" + "\n" +
                    "" + "\n" +
                    "n = int(input())" + "\n" +
                    "print((n + 5) // 6, n);" + "\n" +
                    "Задача C. Алфавит" + "\n" +
                    "В этой задаче нужно было найти максимальное число букв из заданной строки, которое совпадает с началом алфавита. Для удобства, алфавит был приведен целиком в условии задачи. Проще всего решить задачу циклом, который на каждой итерации проверяет очередной символ строки. Как только символ не совпадает с очередным символом алфавита, нужно остановить цикл." + "\n" +
                    "" + "\n" +
                    "Пример кода на языке Python:" + "\n" +
                    "" + "\n" +
                    "s = input()" + "\n" +
                    "a = \"abcdefghijklmnopqrstuvwxyz\"" + "\n" +
                    "res = 0" + "\n" +
                    "for i in range(len(s)):" + "\n" +
                    "    if s[i] == a[i]:" + "\n" +
                    "        res += 1" + "\n" +
                    "    else:" + "\n" +
                    "        break" + "\n" +
                    "print(res)" + "\n" +
                    "Задача D. Верные утверждения" + "\n" +
                    "Эта задача вызвала, к сожалению, больше вопросов у участников, чем предполагало жюри олимпиады. Давайте разбираться, что в ней происходит. Есть n утверждений вида \"Ровно ai из этих утверждений верны\". Нужно найти максимальное число верных утверждений, которое может быть среди них. Пусть среди них ровно k верных утверждений, тогда все утверждения, для которых ai=k верны, а остальные нет. Такое может быть, если утверждений, для которых ai=k ровно k. Посчитаем для каждого k, сколько всего утверждений, в которых ai=k, после чего переберем подходящие k и выберем максимальное." + "\n" +
                    "" + "\n" +
                    "Отдельной проблемой для многих участников стал случай, когда ответ равен 0. Это возможно, если ни один ответ больше 0 не может быть правильным, и при этом нет утверждений, в которых ai=0. Если же у нас есть утверждения, в которых ai=0, то ответ 0 также не может быть правильным, поэтому надо вывести −1." + "\n" +
                    "" + "\n" +
                    "Пример кода на языке Python:" + "\n" +
                    "" + "\n" +
                    "n = int(input())" + "\n" +
                    "a = [int(x) for x in input().split()]" + "\n" +
                    "c = [0] * (n + 1)" + "\n" +
                    "for x in a:" + "\n" +
                    "    c[x] += 1" + "\n" +
                    " " + "\n" +
                    "res = -1" + "\n" +
                    "for k in range(n + 1):" + "\n" +
                    "    if c[k] == k:" + "\n" +
                    "        res = k" + "\n" +
                    "        " + "\n" +
                    "print(res)" + "\n" +
                    "Задача E. Овечка" + "\n" +
                    "Для начала найдем позицию, в которой находится овечка. Затем будем двигаться от нее влево и вправо, пока не встретим камень или конец поля. Получим левую и правую границы отрезка, на котором может пастись овечка. Тогда ответом будет r−l+1." + "\n" +
                    "" + "\n" +
                    "Пример кода на языке Python:" + "\n" +
                    "" + "\n" +
                    "s = input()" + "\n" +
                    "i = s.find(\"@\")" + "\n" +
                    "l = i - 1" + "\n" +
                    "while l >= 0 and s[l] == '.':" + "\n" +
                    "    l -= 1" + "\n" +
                    "r = i + 1" + "\n" +
                    "while r < len(s) and s[r] == '.':" + "\n" +
                    "    r += 1" + "\n" +
                    "" + "\n" +
                    "print(r - l - 1)" + "\n" +
                    "Задача F. Сортировка сыра" + "\n" +
                    "Есть много способов решать эту задачу. Самый простой и эффективный способ такой. Пойдем справа налево. Последний столбик уменьшать смысла нет, так как он должен быть самым высоким. Посмотрим на предпоследний столбик. Если он меньше, чем последний, то его опять же нет смысла уменьшать, а если выше — то его следует уменьшить до той же высоты. И так далее, каждый раз сравниваем столбик со следующим, если он выше, то уменьшаем его, чтобы они стали равны." + "\n" +
                    "" + "\n" +
                    "Пример кода на языке Python:" + "\n" +
                    "" + "\n" +
                    "n = int(input())" + "\n" +
                    "a = [int(x) for x in input().split()]" + "\n" +
                    "" + "\n" +
                    "res = 0" + "\n" +
                    "for i in range(n - 2, -1, -1):" + "\n" +
                    "    if a[i] > a[i + 1]:" + "\n" +
                    "        res += a[i] - a[i + 1]" + "\n" +
                    "        a[i] = a[i + 1]" + "\n" +
                    "" + "\n" +
                    "print(res)" + "\n" +
                    "Задача G. Задачи для олимпиады" + "\n" +
                    "Довольно сложная задача. Можно решать ее, разбирая множество частных случаев, но так достаточно легко допустить ошибку. Чтобы упростить решение, можно зафиксировать один из параметров так, чтобы остальные вычислялись легко. Например, можно перебрать число туров, которые мы хотим провести. Если это число равно x, то как проверить, хватит ли у нас задач? Посмотрим на первую задачу. Это должна быть задача сложности 1 или 2. Понятно, что надо в первую очередь брать задачи сложности 1, потому что задачи сложности 2 можно использовать еще и на второй позиции. Тогда нам надо использовать q=max(0,x−a[0]) задач сложности 2. Теперь так же посмотрим, хватает ли нам задач на вторую позицию, при этом в первую очередь используем задачи сложности 2 и 3, а затем уже 4. В конце посмотрим, хватает ли нам задач сложности 4 и 5 на третью позицию." + "\n" +
                    "" + "\n" +
                    "Пример кода на языке Python:" + "\n" +
                    "" + "\n" +
                    "a = [int(input()) for i in range(5)]" + "\n" +
                    "x = 0" + "\n" +
                    "while True:" + "\n" +
                    "    x += 1" + "\n" +
                    "    if a[0] + a[1] < x:  # хватает ли задач на позицию 1" + "\n" +
                    "        break" + "\n" +
                    "    q = max(0, x - a[0])" + "\n" +
                    "    if a[1] + a[2] + a[3] - q < x:  # хватает ли задач на позицию 2" + "\n" +
                    "        break" + "\n" +
                    "    w = max(0, x - a[1] - a[2] + q)" + "\n" +
                    "    if a[3] + a[4] - w < x:  # хватает ли задач на позицию 3" + "\n" +
                    "        break" + "\n" +
                    "print(x - 1)", 6, 34, 11, 3),
            new Post(6, "Codeforces: Our Steps After DDOS Attack", "Hello." + "\n" +
                    "" + "\n" +
                    "As I wrote into a comment, last round we are faced with a strong DDOS-attack which ruined the competition. I don't know who did it, I also don't know reasons to do it. I'm very upset about the situation and ready to make an effort to be prepared for such issues." + "\n" +
                    "" + "\n" +
                    "I spend a lot of time to be ready for such incidents." + "\n" +
                    "" + "\n" +
                    "Here are steps you nee to do to be ready for unexpected failures:" + "\n" +
                    "" + "\n" +
                    "Join our telegram channel by the link https://t.me/codeforces_official to read urgent news." + "\n" +
                    "Be sure that you know the password of your Codeforces account. If you don't remember it, just use the password recovery feature. Please, do it right now." + "\n" +
                    "I've implemented a minimalistic website for replacing the main site in case of emergencies. Now you can only read problems, view your submissions (without any details), submit codes. Probably, later I'll add some more features, but anyway, the minimalistic version will have only vital features to take part in a contest. I've deployed it in several places, you can visit any of them by the links: http://m1.codeforces.com, http://m2.codeforces.com, http://m3.codeforces.com. If any of them is unavailable, just use another. Do not use them if the main website is alive." + "\n" +
                    "UPD: Now you can enter minimalistic websites without a password. In this case, an email with a secret enter link will be sent to you.", 1, 3010, 23, 166),
            new Post(7, "The best practice in the wild North West of Russia", "Special for Erofei", 7, 1000, 1, 100),
            new Post(8, "The best beard in the wild North West of Russia", "Special for Gera", 9, 1000, 1, 100)
    );

    public static void addData(HttpServletRequest request, Map<String, Object> data) {
        data.put("users", USERS);
        data.put("posts", POSTS);

        for (User user : USERS) {
            if (Long.toString(user.getId()).equals(request.getParameter("logged_user_id"))) {
                data.put("user", user);
            }
        }


    }
}
