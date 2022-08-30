package com.mango.demo.stu.demo;

import com.alibaba.fastjson.JSON;
import com.mango.demo.stu.demo.po.Terms;

import javax.mail.search.AndTerm;
import javax.mail.search.SearchTerm;
import java.util.HashMap;
import java.util.Objects;

/**
 * @Author Mango
 * @Date 2022/8/23 23:17
 */
public class JsonToMap {

    public static void main(String[] args) {
        String json="{\n" +
                "  \"logic\": \"AndTerm\",\n" +
                "  \"searchTerms\": [\n" +
                "    {\n" +
                "      \"FromStringTerm\": \"test_hao@sina.cn\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"SubjectTerm\": \"测验1\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        Terms searchTerms = JSON.parseObject(json, Terms.class);
        HashMap<String, String> map = new HashMap<>();
        String logic = searchTerms.getLogic();
        System.out.println(logic);
        for (String term : searchTerms.getSearchTerms()) {
            String substring = term.substring(2, term.length() - 2);
            String[] split = substring.split("\":\"");
            System.out.println(split[0]);
            System.out.println(split[1]);
            map.put(split[0],split[1]);
        }
        SearchTerm searchTerm = createTerm(logic,map);
        SearchTerm andTerm = new AndTerm(null, null);
        System.out.println(searchTerm);

    }

    private static SearchTerm createTerm(String logic, HashMap<String, String> map) {
        SearchTerm[] array = map.entrySet().parallelStream()
                .map(term -> {
                            try {
                                return (SearchTerm) Class.forName("javax.mail.search." + term.getKey()).getDeclaredConstructor(String.class).newInstance(term.getValue());
                            } catch (Exception e) {
                                return null;
                            }
                        }
                ).filter(Objects::nonNull).toArray(SearchTerm[]::new);
        try {
            return  (SearchTerm) Class.forName("javax.mail.search." + logic).getDeclaredConstructor(SearchTerm[].class).newInstance((Object) array);
        } catch (Exception e) {
            e.printStackTrace();
        }
           return null;

    }
}
