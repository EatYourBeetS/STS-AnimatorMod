package eatyourbeets.cards.animator.series.MadokaMagica;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.animator.curse.special.Curse_GriefSeed;
import eatyourbeets.cards.animator.special.NagisaMomoe_Charlotte;
import eatyourbeets.cards.animator.special.NagisaMomoe_CharlotteAlt;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.ui.common.EYBCardPopupActions;
import eatyourbeets.utilities.GameActions;

public class NagisaMomoe extends AnimatorCard
{
    public static final EYBCardData DATA = Register(NagisaMomoe.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                data.AddPopupAction(new EYBCardPopupActions.MadokaMagica_Witch(NagisaMomoe_Charlotte.DATA));
                data.AddPreview(new NagisaMomoe_Charlotte(), true);
                data.AddPreview(new NagisaMomoe_CharlotteAlt(), true);
                data.AddPreview(new Curse_GriefSeed(), false);
            });

    public NagisaMomoe()
    {
        super(DATA);

        Initialize(0, 0, 1);
        SetUpgrade(0, 0, 1);

        SetAffinity_Star(2);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ChannelOrbs(() ->
        {
            switch (rng.random(2))
            {
                case 0: return new Lightning();
                case 1: return new Frost();
                case 2: return new Fire();
                default: throw new RuntimeException("Invalid case");
            }
        }, magicNumber + (CheckSpecialCondition(true) ? 1 : 0));
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        for (AbstractCard c : player.hand.group)
        {
            if (NagisaMomoe_Charlotte.DATA.IsCard(c) || NagisaMomoe_CharlotteAlt.DATA.IsCard(c) || Curse_GriefSeed.DATA.IsCard(c))
            {
                return true;
            }
        }

        return false;
    }
}