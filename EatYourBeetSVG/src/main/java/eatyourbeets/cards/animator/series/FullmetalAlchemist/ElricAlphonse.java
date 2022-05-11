package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.ElricAlphonse_Alt;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.affinity.AbstractAffinityPower;
import eatyourbeets.ui.common.EYBCardPopupActions;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class ElricAlphonse extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ElricAlphonse.class)
            .SetSkill(0, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                data.AddPopupAction(new EYBCardPopupActions.FullmetalAlchemist_Alphonse(6, ElricEdward.DATA, ElricAlphonse_Alt.DATA));
                data.AddPreview(new ElricAlphonse_Alt(), true);
            });

    public ElricAlphonse()
    {
        super(DATA);

        Initialize(0, 2, 6);
        SetUpgrade(0, 0, 1);

        SetAffinity_Blue(1);
        SetAffinity_Light(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);

        if (CheckSpecialCondition(true))
        {
            GameActions.Bottom.GainIntellect(1);
        }

        if (info.TryActivateStarter() && info.TryActivateLimited()) {
            GameActions.Last.SelectFromPile(name, 1, p.discardPile)
                    .SetFilter(c -> !GameUtilities.IsSealed(c))
                    .AddCallback(cards ->
                    {
                        for (AbstractCard c : cards)
                        {
                            GameActions.Bottom.SealAffinities(c, false, true);
                        }
                    });
        }

    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return CombatStats.Affinities.GetPowerAmount(Affinity.Blue) < magicNumber;
    }
}