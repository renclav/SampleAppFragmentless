package com.touchnote.renclav.touchnotesampleapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import com.touchnote.renclav.touchnotesampleapp.data.Clue;
import com.touchnote.renclav.touchnotesampleapp.data.source.CluesDataSource;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Renclav on 2016/11/07.
 */

public class FakeCluesRemoteDataSource implements CluesDataSource {

    private static final String JSON_RESPONSE = "[\n" +
            "  {\n" +
            "    \"id\": \"56e83e157b68737318595d7b\",\n" +
            "    \"description\": \"Itaque rerum quod eius quam ut aut rem.\\nQui adipisci ad et.\\nMagni dolores aspernatur vel.\\nEnim alias ut quasi officia.\\n \\rMaxime non debitis.\\nVoluptas voluptatem sunt quis.\\nEveniet velit vel ut eaque sit.\\nEt sit non.\\nSaepe et ut deleniti perferendis distinctio dolorem vitae ut.\",\n" +
            "    \"date\": \"2016-03-20T00:00:00.000Z\",\n" +
            "    \"tags\": [\n" +
            "      \"facilis\",\n" +
            "      \"soluta\",\n" +
            "      \"repellat\",\n" +
            "      \"voluptatem\",\n" +
            "      \"rem\"\n" +
            "    ],\n" +
            "    \"title\": \"enim quasi non incidunt veritatis magni rerum autem voluptatibus perferendis ratione\",\n" +
            "    \"image\": \"https://tn-mobile-cms-dev.s3-eu-west-1.amazonaws.com/shared/illustrations/base/illustration-750-00/fullRes.jpg\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"56e83e157b68737318595d79\",\n" +
            "    \"description\": \"Nobis soluta possimus dicta nihil excepturi distinctio sint autem dolorum.\\nNisi nihil est veniam rerum qui adipisci voluptatem.\\nImpedit sapiente quisquam est totam culpa.\\nSed ut quibusdam.\\nSed dolores corrupti ratione deserunt vero repellat amet.\",\n" +
            "    \"date\": \"2016-03-19T00:00:00.000Z\",\n" +
            "    \"tags\": [\n" +
            "      \"iste\",\n" +
            "      \"sed\",\n" +
            "      \"fugiat\",\n" +
            "      \"ut\",\n" +
            "      \"dolores\"\n" +
            "    ],\n" +
            "    \"title\": \"veniam impedit quisquam est\",\n" +
            "    \"image\": \"https://tn-mobile-cms-dev.s3-eu-west-1.amazonaws.com/shared/illustrations/base/illustration-750-01/fullRes.jpg\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"56e83e157b68737318595d6e\",\n" +
            "    \"description\": \"Ea molestiae similique fugit eligendi qui alias mollitia hic sunt.\\nOfficia natus laudantium nihil excepturi sunt.\\nRerum doloremque quas quis.\\nReiciendis voluptates nihil.\",\n" +
            "    \"date\": \"2016-03-18T00:00:00.000Z\",\n" +
            "    \"tags\": [\n" +
            "      \"iusto\",\n" +
            "      \"et\",\n" +
            "      \"nihil\",\n" +
            "      \"vero\",\n" +
            "      \"dolorem\"\n" +
            "    ],\n" +
            "    \"title\": \"sed consequatur pariatur soluta aperiam veniam odio\",\n" +
            "    \"image\": \"https://tn-mobile-cms-dev.s3-eu-west-1.amazonaws.com/shared/illustrations/base/illustration-750-02/fullRes.jpg\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"56e83e15cc8208b62d657982\",\n" +
            "    \"description\": \"Neque sunt ducimus voluptatem possimus ea odit perferendis reiciendis praesentium.\\nExcepturi quos ea qui dicta.\\nEt laborum vel ullam omnis sunt deleniti.\\nAlias non dignissimos.\\nQuia porro odio praesentium optio aut.\\n \\rOmnis sit sint harum fuga ipsum.\\nOfficia nam enim minima.\\nNumquam molestiae ex libero nisi ab cupiditate.\\nEt magni aut ea et tempora odit sit facere quis.\",\n" +
            "    \"date\": \"2016-03-17T00:00:00.000Z\",\n" +
            "    \"tags\": [\n" +
            "      \"qui\",\n" +
            "      \"voluptatem\",\n" +
            "      \"dolor\",\n" +
            "      \"eum\",\n" +
            "      \"dolorem\"\n" +
            "    ],\n" +
            "    \"title\": \"sit sed cumque magnam maiores modi nisi hic quos\",\n" +
            "    \"image\": \"https://tn-mobile-cms-dev.s3-eu-west-1.amazonaws.com/shared/illustrations/base/illustration-750-03/fullRes.jpg\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"56e83e15cc8208b62d657980\",\n" +
            "    \"description\": \"Voluptate vero ipsum porro ad voluptatibus omnis distinctio.\\nEst voluptatem itaque ut voluptatem maxime voluptates rem fuga.\\nVel omnis necessitatibus velit voluptatem ullam.\\nNisi vel fugit vitae reprehenderit nemo ea laboriosam.\",\n" +
            "    \"date\": \"2016-03-16T00:00:00.000Z\",\n" +
            "    \"tags\": [\n" +
            "      \"et\",\n" +
            "      \"deleniti\",\n" +
            "      \"porro\",\n" +
            "      \"molestiae\",\n" +
            "      \"vitae\"\n" +
            "    ],\n" +
            "    \"title\": \"ullam id pariatur esse rem autem animi ratione\",\n" +
            "    \"image\": \"http://pipsum.com/200x200.jpg\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"56e83e157b68737318595d6c\",\n" +
            "    \"description\": \"Unde velit sint odit.\\nBlanditiis est quo dolore eligendi accusantium voluptatem et cum qui.\\nSequi quod dolorum eligendi necessitatibus cupiditate tempora corrupti.\\n \\rHic explicabo mollitia cupiditate laborum omnis quis.\\nVoluptas velit neque dolores voluptas quia.\\nNatus voluptates vero eligendi nisi.\\nEt eveniet distinctio eligendi culpa.\\nEarum commodi dignissimos asperiores odit delectus minima fugit aut.\\nVelit cupiditate et minus commodi reprehenderit accusantium.\\n \\rIusto assumenda aut totam qui voluptatum ut dolorem.\\nVeniam earum ut.\\nQuae alias dolor odit voluptatum explicabo ducimus ut facilis officiis.\\nVoluptas et ab magnam.\\nEum et quis.\\nSequi nemo ipsum quo odio modi atque voluptatem cupiditate voluptate.\",\n" +
            "    \"date\": \"2016-03-15T00:00:00.000Z\",\n" +
            "    \"tags\": [\n" +
            "      \"voluptate\",\n" +
            "      \"voluptatem\",\n" +
            "      \"officia\",\n" +
            "      \"quam\",\n" +
            "      \"sapiente\"\n" +
            "    ],\n" +
            "    \"title\": \"nam itaque velit qui veritatis et occaecati esse consequuntur exercitationem vel\",\n" +
            "    \"image\": \"https://tn-mobile-cms-dev.s3-eu-west-1.amazonaws.com/shared/illustrations/base/illustration-750-04/fullRes.jpg\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"56e83e157b68737318595d6a\",\n" +
            "    \"description\": \"\",\n" +
            "    \"date\": \"2016-03-10T00:00:00.000Z\",\n" +
            "    \"tags\": [\n" +
            "      \"ad\",\n" +
            "      \"omnis\",\n" +
            "      \"eligendi\",\n" +
            "      \"delectus\",\n" +
            "      \"ut\"\n" +
            "    ],\n" +
            "    \"title\": \"saepe explicabo ducimus aliquam tenetur ea quaerat molestiae\",\n" +
            "    \"image\": \"https://tn-mobile-cms-dev.s3-eu-west-1.amazonaws.com/shared/illustrations/base/illustration-750-05/fullRes.jpg\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"56e83e15cc8208b62d65797e\",\n" +
            "    \"description\": \"Autem et consequatur hic maxime tempore.\\nExplicabo repellendus nemo asperiores consequuntur at et quia corporis sint.\\nAlias et ipsum molestiae similique quod.\\nOmnis magnam excepturi quo cupiditate libero et et.\\n \\rConsequatur aut voluptatum id veritatis voluptatem debitis.\\nSimilique aut dolor enim explicabo pariatur id dolore voluptate sequi.\\nRerum ullam quasi sequi amet voluptatem.\\nQui omnis quo voluptatibus sapiente.\\n \\rDeleniti repellat nihil inventore minus quibusdam perferendis.\\nSed consectetur reprehenderit sint optio.\\nQuia aut temporibus eum.\\nOptio vel et beatae atque tenetur sit quo aut voluptatibus.\",\n" +
            "    \"date\": \"2016-03-09T00:00:00.000Z\",\n" +
            "    \"tags\": [\n" +
            "      \"vel\",\n" +
            "      \"sed\",\n" +
            "      \"est\",\n" +
            "      \"minus\",\n" +
            "      \"ut\"\n" +
            "    ],\n" +
            "    \"title\": \"minima et modi at deserunt expedita\",\n" +
            "    \"image\": \"https://tn-mobile-cms-dev.s3-eu-west-1.amazonaws.com/shared/illustrations/base/illustration-750-06/fullRes.jpg\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"56e83e157b68737318595d68\",\n" +
            "    \"description\": \"Corrupti nihil ipsum officia optio illo.\\nAspernatur et vitae fuga laudantium itaque.\\nAliquam ipsam accusantium est dolore.\\nEt et rem sed ipsa nostrum.\\nImpedit officiis fugit temporibus ut numquam qui dolores.\",\n" +
            "    \"date\": \"2016-03-08T00:00:00.000Z\",\n" +
            "    \"tags\": [\n" +
            "      \"nemo\",\n" +
            "      \"et\",\n" +
            "      \"tempora\",\n" +
            "      \"nulla\",\n" +
            "      \"dolorem\"\n" +
            "    ],\n" +
            "    \"title\": \"ipsa modi et consequatur repellendus impedit atque recusandae adipisci voluptas\",\n" +
            "    \"image\": \"https://tn-mobile-cms-dev.s3-eu-west-1.amazonaws.com/shared/illustrations/base/illustration-750-07/fullRes.jpg\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"56e83e15cc8208b62d657979\",\n" +
            "    \"description\": \"Animi nemo tempore neque.\\nMaxime quia recusandae quae temporibus.\\nIncidunt sed ratione.\\nDolore voluptatem nihil et qui asperiores.\\n \\rSed qui omnis non veritatis et.\\nNon qui hic ea.\\nVoluptas quas repudiandae at eum.\\nAut non repellat ipsum beatae veniam quae et et esse.\\nVoluptas at labore et.\",\n" +
            "    \"date\": \"2016-03-03\",\n" +
            "    \"tags\": [\n" +
            "      \"et\",\n" +
            "      \"quisquam\",\n" +
            "      \"corrupti\",\n" +
            "      \"et\",\n" +
            "      \"quasi\"\n" +
            "    ],\n" +
            "    \"title\": \"quia qui architecto sapiente aspernatur\",\n" +
            "    \"image\": \"https://tn-mobile-cms-dev.s3-eu-west-1.amazonaws.com/shared/illustrations/base/illustration-750-08/fullRes.jpg\"\n" +
            "  }\n" +
            "]";


    private static FakeCluesRemoteDataSource INSTANCE;

    private final Moshi moshi;
    private final JsonAdapter<List<Clue>> jsonAdapter;

    public static FakeCluesRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FakeCluesRemoteDataSource();
        }
        return INSTANCE;
    }

    private FakeCluesRemoteDataSource()
    {
        moshi = new Moshi.Builder().build();
        ParameterizedType parameterizedType = Types.newParameterizedType(List.class, Clue.class);
        jsonAdapter = moshi.adapter(parameterizedType);
    }

    @Override
    public Observable<List<Clue>> getClues() {
        return Observable.fromCallable(new Callable<List<Clue>>() {
            @Override
            public List<Clue> call() throws Exception {
                return jsonAdapter.fromJson(JSON_RESPONSE);
            }
        });
    }

    @Override
    public Observable<Clue> getClue(@NonNull final String clueId) {
        return getClues().flatMap(new Func1<List<Clue>, Observable<Clue>>() {
            @Override
            public Observable<Clue> call(List<Clue> clues) {
                return Observable.from(clues);
            }
        }).firstOrDefault(null, new Func1<Clue, Boolean>() {
            @Override
            public Boolean call(Clue clue) {
                return TextUtils.equals(clueId, clue.getId());
            }
        });
    }

    @Override
    public void saveClues(List<Clue> clues) {
        //NO OP
    }

    @Override
    public void refreshClues() {
        //NO OP
    }
}
