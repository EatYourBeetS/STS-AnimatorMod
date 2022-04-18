package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.resources.GR;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.GameActions;


public class Nikolay extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Nikolay.class)
            .SetAttack(2, CardRarity.COMMON)
            .SetSeriesFromClassPackage()
            .ModifyRewards((data, rewards) ->
            {
                final int copies = data.GetTotalCopies(player.masterDeck);
                if (copies > 0 && copies < data.MaxCopies)
                {
                    GR.Common.Dungeon.TryReplaceFirstCardReward(rewards, copies < 2 ? 0.12f : 0.08f, true, data);
                }
            });

    public Nikolay()
    {
        super(DATA);

        Initialize(11, 0, 4);
        SetUpgrade(2, 0);

        SetAffinity_Red(1, 1, 0);
        SetAffinity_Green(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HEAVY);
        GameActions.Bottom.RetainPower(Affinity.Red);
        GameActions.Bottom.ModifyAllCopies(cardID)
        .AddCallback(c ->
        {
            if (uuid != c.uuid)
            {
                GameActions.Bottom.Motivate(c, 1);
            }
        });

        if ((ForceStance.IsActive() || info.IsSynergizing) && info.TryActivateSemiLimited())
        {
            GameActions.Bottom.GainBlock(magicNumber);
        }
    }
}