package eatyourbeets.cards.animator.beta.series.AngelBeats;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.status.Status_Dazed;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.misc.CardMods.AfterLifeMod;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class MasamiIwasawa extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MasamiIwasawa.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Status_Dazed(), false));

    public MasamiIwasawa()
    {
        super(DATA);

        Initialize(0, 12, 1, 2);
        SetUpgrade(0, 3, 0, 0);

        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Light(1, 0, 2);

        SetEthereal(true);
        AfterLifeMod.Add(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);

        GameActions.Bottom.MakeCardInDrawPile(new Status_Dazed())
                .Repeat(secondaryValue);

        if (IsStarter())
        {
            GameActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(), CombatStats.ControlPile.Contains(this) ? secondaryValue : magicNumber);
        }
    }
}