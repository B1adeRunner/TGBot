package ru.skynet.bot.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PrimitiveContent {
    private String content;
    private String description;
    private String fullContentMessage;

    public static List<String> buttsPrimitiveContent = Arrays.asList(
            "(_!_) - жопа обыкновенная",
            "(__!__) - жирная жопа",
            "(_._) - жопа плоская",
            "(!) - тощая жопа",
            "{_!_} - шикарная жопа",
            "(_*_) - геморройная жопа",
            "(_zzz_) - жопа усталая",
            "(_?_) - жопа безмозглая",
            "(_о_) - жопа пользованная",
            "(_0_) - много раз пользованная",
            "(_$_) - новорусская жопа",
            "(_x_) - поцелуй меня в жопу!",
            "(_Х_) - оставь мою жопу в покое!",
            "(_^_) - заносчивая жопа.",
            "(ЖоЖ) - волосатая жопа",
            ". - жопа, вид из космоса",
            "(_~_) - хитрая жопа",
            "(_Smile_) - очень хитрая жопа",
            "(_e=mc2_) - умная жопа",
            "(_Ъ_) - твердая жопа",
            "(_Ь_) - мягкая жопа",
            "(_GO_) - иди в жопу",
            "->(_!_) - пошел в жопу",
            "(_!_)-> - как из жопы",
            "(_SOS_) - жопа в беде",
            "{-------O-------} - очень большая жопа",
            "(_100%_) - полная жопа",
            "(_!_)] - жопа с ручкой",
            "(_!_)S - жопа с ручкой",
            "->(_!_)-> - через жопу (делать что-либо)",
            "(_=_) какая, в жопу, разница?",
            "(_Я_) - Я в жопе...",
            "(__Я__) - Я в полной жопе...",
            "(_Мы_) - Мы в жопе:",
            "к(_!_)Э - жопа с ушами",
            "c( o ) - жопа с ручкой",
            "(_!_)(___!___)(_!_)(__!__) - кинотеатр"
    );
    public static List<String> boobsPrimitiveContent = Arrays.asList(
            "(.)(.) - просто сиси"
    );
}