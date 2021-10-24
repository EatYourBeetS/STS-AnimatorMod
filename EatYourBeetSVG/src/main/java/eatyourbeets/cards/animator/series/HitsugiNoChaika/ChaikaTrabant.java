package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class ChaikaTrabant extends AnimatorCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final EYBCardData DATA = Register(ChaikaTrabant.class)
            .SetAttack(2, CardRarity.RARE, EYBAttackType.Elemental)
            .SetSeriesFromClassPackage();

    private AbstractMonster enemy;

    public ChaikaTrabant()
    {
        super(DATA);

        Initialize(10, 0, 1, 10);
        SetUpgrade(0, 0, 1, 4);

        SetAffinity_Fire();
        SetAffinity_Light();
        SetAffinity_Mind();

        tags.add(GR.Enums.CardTags.IGNORE_PEN_NIB);
        SetProtagonist(true);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.DealDamageToAll(DamageInfo.createDamageMatrix(secondaryValue, false),
        damageTypeForTurn, AttackEffects.FIRE).SetPiercing(true, false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        m.useFastShakeAnimation(0.5f);

        ChaikaTrabant other = (ChaikaTrabant) makeStatEquivalentCopy();
        other.enemy = m;
        other.tags.remove(GR.Enums.CardTags.IGNORE_PEN_NIB);
        CombatStats.onStartOfTurnPostDraw.Subscribe(other);
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        GameEffects.Queue.ShowCardBriefly(makeStatEquivalentCopy());
        CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);

        GameActions.Bottom.Callback(() ->
        {
            if (enemy == null || GameUtilities.IsDeadOrEscaped(enemy))
            {
                enemy = GameUtilities.GetRandomEnemy(true);

                if (enemy == null)
                {
                    return;
                }
            }

            this.applyPowers();
            this.calculateCardDamage(enemy);

            GameActions.Bottom.DealDamage(this, enemy, AttackEffects.FIRE);

            for (PowerHelper commonDebuff : GameUtilities.GetCommonDebuffs())
            {
                GameActions.Bottom.ApplyPower(enemy, commonDebuff.Create(player, player, magicNumber));
            }
        });
    }
}