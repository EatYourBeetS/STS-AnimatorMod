package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.subscribers.OnLoseHpSubscriber;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Kamina extends AnimatorCard implements OnLoseHpSubscriber, OnStartOfTurnPostDrawSubscriber
{
    public static final EYBCardData DATA = Register(Kamina.class).SetAttack(1, CardRarity.UNCOMMON).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.GurrenLagann);
    public static int ATTACK_DELAY = 2;
    private int counter;
    private AbstractMonster enemy;

    public Kamina()
    {
        super(DATA);

        Initialize(41, 0, ATTACK_DELAY, 3);
        SetUpgrade(15, 0, 0 , 0);

        SetAffinity_Red(2, 0, 6);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        m.useFastShakeAnimation(0.5f);

        Kamina other = (Kamina) makeStatEquivalentCopy();
        other.counter = ATTACK_DELAY;
        other.enemy = m;
        other.tags.remove(GR.Enums.CardTags.IGNORE_PEN_NIB);
        CombatStats.onLoseHp.Subscribe(other);
        CombatStats.onStartOfTurnPostDraw.Subscribe(other);

        GameActions.Bottom.ApplyFrail(null, p, secondaryValue);
        GameActions.Bottom.ApplyVulnerable(null, p, secondaryValue);
    }

    @Override
    public void OnStartOfTurnPostDraw() {
        GameActions.Bottom.Callback(() ->
        {
            counter -= 1;
            if (counter <= 0) {
                if (enemy == null || GameUtilities.IsDeadOrEscaped(enemy))
                {
                    enemy = GameUtilities.GetRandomEnemy(true);

                    if (enemy == null)
                    {
                        return;
                    }
                }
                GameEffects.Queue.ShowCardBriefly(makeStatEquivalentCopy());
                this.applyPowers();
                this.calculateCardDamage(enemy);

                GameActions.Bottom.DealDamage(this, enemy, AttackEffects.PUNCH).AddCallback(() ->
                        GameActions.Bottom.Add(new ShakeScreenAction(0.3f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED)));
                CombatStats.onLoseHp.Unsubscribe(this);
                CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
            }
        });
    }

    @Override
    public int OnLoseHp(int damageAmount) {
        if (damageAmount > 0) {
            CombatStats.onLoseHp.Unsubscribe(this);
            CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
            GameEffects.Queue.ShowCardBriefly(makeStatEquivalentCopy());
        }
        return damageAmount;
    }
}