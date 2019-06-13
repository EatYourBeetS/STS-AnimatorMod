package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.AnimatorResources;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.interfaces.Hidden;

import java.util.ArrayList;

public abstract class Vesta_Elixir extends AnimatorCard implements Hidden
{
    public static final String ID = CreateFullID(Vesta_Elixir.class.getSimpleName());

    private static ArrayList<Vesta_Elixir> subTypes = null;

    public Vesta_Elixir(String id)
    {
        super(AnimatorResources.GetCardStrings(id), id, AnimatorResources.GetCardImage(ID), 0, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);

        this.exhaust = true;
    }

    public static CardGroup GetCardGroup()
    {
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        if (subTypes == null)
        {
            subTypes = new ArrayList<>();
            subTypes.add(new Vesta_Elixir_0());
            subTypes.add(new Vesta_Elixir_1());
            subTypes.add(new Vesta_Elixir_2());
            subTypes.add(new Vesta_Elixir_3());
        }

        group.group.addAll(subTypes);

        return group;
    }

    @Override
    public boolean canUpgrade()
    {
        return false;
    }

    @Override
    public void upgrade()
    {

    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster)
    {

    }
}