package eatyourbeets.cards.animatorClassic.series.HitsugiNoChaika;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class ChaikaTrabant extends AnimatorClassicCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final EYBCardData DATA = Register(ChaikaTrabant.class).SetAttack(2, CardRarity.RARE, EYBAttackType.Elemental);

    private AbstractMonster enemy;

    public ChaikaTrabant()
    {
        super(DATA);

        Initialize(21, 0, 6, 2);
        SetUpgrade(7, 0, 0, 0);
        SetScaling(2, 0, 0);

        tags.add(GR.Enums.CardTags.IGNORE_PEN_NIB);

        SetSpellcaster();
        SetSeries(CardSeries.HitsugiNoChaika);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.DealDamageToAll(DamageInfo.createDamageMatrix(magicNumber, false),
        damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE).SetPiercing(true, false);
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

            GameActions.Bottom.DealDamage(this, enemy, AbstractGameAction.AttackEffect.FIRE);
            GameActions.Bottom.ApplyWeak(player, enemy, 1);
            GameActions.Bottom.ApplyVulnerable(player, enemy, 1);
        });
    }
}