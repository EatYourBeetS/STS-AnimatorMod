package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.cards.animator.beta.special.SheerCold;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.animator.NegateBlockPower;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class AyakaKamisato extends AnimatorCard {
    public static final EYBCardData DATA = Register(AyakaKamisato.class).SetAttack(2, CardRarity.RARE, EYBAttackType.Piercing).SetSeriesFromClassPackage();
    private static final int NO_BLOCK_TURNS = 2;
    static
    {
        DATA.AddPreview(new SheerCold(), false);
    }

    public AyakaKamisato() {
        super(DATA);

        Initialize(12, 0, 2, 3);
        SetUpgrade(1, 0, 0, 1);
        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Green(1, 0, 0);
        SetAffinity_Orange(2, 0, 1);

        SetAffinityRequirement(Affinity.Blue, 4);

        SetExhaust(true);
    }

    @Override
    public void initializeDescription()
    {
        super.initializeDescription();

        if (cardText != null)
        {
            tooltips.add(GR.Tooltips.Freezing);
        }
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing) {

        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.DealDamageToRandomEnemy(this, i % 2 == 0 ? AttackEffects.SLASH_VERTICAL : AttackEffects.SLASH_DIAGONAL).SetOptions(true, false);
        }
        GameActions.Bottom.GainTemporaryThorns(this.getFrostCount() * secondaryValue);
        GameActions.Bottom.StackPower(new NegateBlockPower(p, NO_BLOCK_TURNS));

        if (CheckAffinity(Affinity.Blue) && CombatStats.TryActivateLimited(cardID))
        {
            AbstractCard c = new SheerCold();
            c.applyPowers();
            c.use(player, null);
        }
    }

    private int getFrostCount() {
        return JUtils.Count(player.orbs, orb -> Frost.ORB_ID.equals(orb.ID));
    }
}