package eatyourbeets.cards.animator.series.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.vfx.combat.OrbFlareEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

import java.util.HashSet;

public class AliceMargatroid extends AnimatorCard
{
    public static final EYBCardData DATA = Register(AliceMargatroid.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public AliceMargatroid()
    {
        super(DATA);

        Initialize(0, 0, 1, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Blue(2);
        SetAffinity_Light(1);

        SetEthereal(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainIntellect(1);
        GameActions.Bottom.Draw(magicNumber);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Delayed.Callback(() ->
        {
            HashSet<AbstractOrb> cache = CombatStats.GetCombatData(cardID, null);
            if (cache == null)
            {
                cache = new HashSet<>();
                CombatStats.SetCombatData(cardID, cache);
            }

            for (AbstractOrb orb : player.orbs)
            {
                if (!cache.contains(orb) && orb.passiveAmount >= 2)
                {
                    GameUtilities.IncreaseOrbPassiveAmount(orb, secondaryValue);
                    GameEffects.List.Add(new OrbFlareEffect(orb, OrbFlareEffect.OrbFlareColor.DARK));
                    cache.add(orb);
                    return;
                }
            }
        });
    }
}

