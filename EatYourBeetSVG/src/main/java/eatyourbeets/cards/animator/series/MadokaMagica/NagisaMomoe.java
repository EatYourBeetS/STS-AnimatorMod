package eatyourbeets.cards.animator.series.MadokaMagica;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.Charlotte;
import eatyourbeets.cards.animator.status.Curse_GriefSeed;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class NagisaMomoe extends AnimatorCard
{
    public static final EYBCardData DATA = Register(NagisaMomoe.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);
    static
    {
        DATA.AddPreview(new Charlotte(), true);
        DATA.AddPreview(new Curse_GriefSeed(), false);
    }

    public NagisaMomoe()
    {
        super(DATA);

        Initialize(0, 0, 3);

        SetExhaust(true);
        SetSynergy(Synergies.MadokaMagica);
        SetSpellcaster();
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.MakeCardInDiscardPile(new Charlotte()).SetUpgrade(upgraded, false);
        GameActions.Bottom.MakeCardInDiscardPile(new Curse_GriefSeed());
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.GainRandomStat(1, upgraded);
        }
    }
}