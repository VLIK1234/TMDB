package github.tmdb.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import github.tmdb.R;

/**
 * Created by ASUS on 21.01.2015.
 */
public class AlphaFragment extends Fragment{

    private static final String EXTRA_MESSAGE = "extra_message";

    TextView textView;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail, container);
        textView = (TextView) v.findViewById(R.id.overview);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        textView.setText("9 мотивационных привычек\n" +
                "\n" +
                "1. Ранний подъем.\n" +
                "Все успешные люди - пташки ранние. Что-то особенное и магическое есть в раннем подъеме. Эта часть дня, когда еще мир не проснулся, - самая важная и вдохновляющая. Те, кто встает до рассвета солнца, утверждают, что их жизнь стала полноценной. Попробуйте и вы вставать рано, и уже через месяц-другой вы будете с жалостью вспоминать те года, когда рассвет встречали в постели.\n" +
                "\n" +
                "2. Увлеченное чтение.\n" +
                "Если в день заменить час сидения перед телевизором или компьютером на чтение полезной и интересной книги, то вы станете самым умным человеком среди своего окружения. Вы будете быстрее находить ответы на вопросы, с вами будет интереснее общаться, многое будет получаться само собой. Как сказал Марк Твен:  Человек, который не читает хороших книг, не имеет преимуществ перед человеком, который не умеет читать.\n" +
                "\n" +
                "    3. Умение упрощать.\n" +
                "    Упрощение - это устранение ненужного и бесполезного. Необходимо уметь упрощать все, что можно и нужно упростить. Хоть это и требует долгой практики, в итоге дает положительный результат. Очищаются память и чувства, вы меньше переживаете и нервничаете. Чем будет проще ваша жизнь, тем больше вы сможете ею наслаждаться.\n" +
                "\n" +
                "    4. Замедление.\n" +
                "    Наслаждаться жизнью в постоянном стрессе и хаосе невозможно. Найдите для себя тихое время, остановитесь, прислушайтесь к себе и своему внутреннему голосу. Обратите внимание на все, что имеет для вас значение. Возьмите за привычку просыпаться рано, когда вокруг тишина, чтобы медитировать, размышлять, созидать. Замедлите темп жизни, и тогда все, за чем вы гнались, само догонит вас.\n");
    }
}
