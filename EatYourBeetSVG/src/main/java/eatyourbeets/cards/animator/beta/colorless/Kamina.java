package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
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

        Initialize(50, 0, ATTACK_DELAY, 3);
        SetUpgrade(10, 0, 0 , 0);

        SetAffinity_Red(1, 0, 6);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
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
                GameActions.Top.RemovePower(player,player, WeakPower.POWER_ID);
                this.applyPowers();
                this.calculateCardDamage(enemy);

                GameActions.Bottom.DealCardDamage(this, enemy, AttackEffects.PUNCH).forEach(d -> d.AddCallback(() ->
                        GameActions.Bottom.Add(new ShakeScreenAction(0.3f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED))));
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