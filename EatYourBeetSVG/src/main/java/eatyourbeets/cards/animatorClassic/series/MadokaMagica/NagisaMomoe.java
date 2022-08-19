package eatyourbeets.cards.animatorClassic.series.MadokaMagica;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animatorClassic.curse.Curse_GriefSeed;
import eatyourbeets.cards.animatorClassic.special.NagisaMomoe_CharlotteAlt;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class NagisaMomoe extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(NagisaMomoe.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);
    static
    {
        DATA.AddPreview(new NagisaMomoe_CharlotteAlt(), true);
        DATA.AddPreview(new Curse_GriefSeed(), false);
    }

    public NagisaMomoe()
    {
        super(DATA);

        Initialize(0, 0, 3);

        SetExhaust(true);
        SetSeries(CardSeries.MadokaMagica);
        SetSpellcaster();
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.MakeCardInDiscardPile(new NagisaMomoe_CharlotteAlt()).SetUpgrade(upgraded, false);
        GameActions.Bottom.MakeCardInDiscardPile(new Curse_GriefSeed());
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.GainRandomAffinityPower(1, upgraded);
        }
    }
}