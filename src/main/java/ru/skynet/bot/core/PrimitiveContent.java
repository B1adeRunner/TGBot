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
            "(_!_) - ���� ������������",
            "(__!__) - ������ ����",
            "(_._) - ���� �������",
            "(!) - ����� ����",
            "{_!_} - �������� ����",
            "(_*_) - ����������� ����",
            "(_zzz_) - ���� �������",
            "(_?_) - ���� ����������",
            "(_�_) - ���� ������������",
            "(_0_) - ����� ��� ������������",
            "(_$_) - ����������� ����",
            "(_x_) - ������� ���� � ����!",
            "(_�_) - ������ ��� ���� � �����!",
            "(_^_) - ���������� ����.",
            "(���) - ��������� ����",
            ". - ����, ��� �� �������",
            "(_~_) - ������ ����",
            "(_Smile_) - ����� ������ ����",
            "(_e=mc2_) - ����� ����",
            "(_�_) - ������� ����",
            "(_�_) - ������ ����",
            "(_GO_) - ��� � ����",
            "->(_!_) - ����� � ����",
            "(_!_)-> - ��� �� ����",
            "(_SOS_) - ���� � ����",
            "{-------O-------} - ����� ������� ����",
            "(_100%_) - ������ ����",
            "(_!_)] - ���� � ������",
            "(_!_)S - ���� � ������",
            "->(_!_)-> - ����� ���� (������ ���-����)",
            "(_=_) �����, � ����, �������?",
            "(_�_) - � � ����...",
            "(__�__) - � � ������ ����...",
            "(_��_) - �� � ����:",
            "�(_!_)� - ���� � �����",
            "c( o ) - ���� � ������",
            "(_!_)(___!___)(_!_)(__!__) - ���������"
    );
    public static List<String> boobsPrimitiveContent = Arrays.asList(
            "(.)(.) - ������ ����"
    );
}