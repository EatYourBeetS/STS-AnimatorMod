package eatyourbeets.cards.animator.beta.series.AngelBeats;

import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.misc.CardMods.AfterLifeMod;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class MasamiIwasawa extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MasamiIwasawa.class).SetSkill(1, CardRarity.COMMON).SetSeriesFromClassPackage();

    public MasamiIwasawa()
    {
        super(DATA);

        Initialize(0, 11, 1, 2);
        SetUpgrade(0, 3, 0, 0);

        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Light(1, 0, 2);

        AfterLifeMod.Add(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);

        GameActions.Bottom.MakeCardInDrawPile(new Dazed())
                .Repeat(secondaryValue);

        if (IsStarter())
        {
            GameActions.Bottom.ApplyVulnerable(p, m, CombatStats.ControlPile.Contains(this) ? secondaryValue : magicNumber);
        }
    }
}