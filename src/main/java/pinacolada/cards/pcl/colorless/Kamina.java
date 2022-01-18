package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import eatyourbeets.interfaces.subscribers.OnLosingHPSubscriber;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.PCLCombatStats;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class Kamina extends PCLCard implements OnLosingHPSubscriber, OnStartOfTurnPostDrawSubscriber
{
    public static final PCLCardData DATA = Register(Kamina.class).SetAttack(1, CardRarity.UNCOMMON).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.GurrenLagann);
    public static int ATTACK_DELAY = 1;
    private int counter;
    private AbstractMonster enemy;

    public Kamina()
    {
        super(DATA);

        Initialize(36, 0, ATTACK_DELAY, 3);
        SetUpgrade(9, 0, 0 , 0);

        SetAffinity_Red(1, 0, 6);
        SetExhaust(true);
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        return IsStarter();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        m.useFastShakeAnimation(0.5f);

        Kamina other = (Kamina) makeStatEquivalentCopy();
        other.counter = ATTACK_DELAY;
        other.enemy = m;
        other.tags.remove(GR.Enums.CardTags.IGNORE_PEN_NIB);
        PCLCombatStats.onLosingHP.Subscribe(other);
        PCLCombatStats.onStartOfTurnPostDraw.Subscribe(other);

        PCLActions.Bottom.ApplyFrail(null, p, secondaryValue);
        PCLActions.Bottom.ApplyVulnerable(null, p, secondaryValue);
    }

    @Override
    public void OnStartOfTurnPostDraw() {
        PCLActions.Bottom.Callback(() ->
        {
            counter -= 1;
            if (counter <= 0) {
                if (enemy == null || PCLGameUtilities.IsDeadOrEscaped(enemy))
                {
                    enemy = PCLGameUtilities.GetRandomEnemy(true);

                    if (enemy == null)
                    {
                        return;
                    }
                }
                PCLGameEffects.Queue.ShowCardBriefly(makeStatEquivalentCopy());
                PCLActions.Top.RemovePower(player,player, VulnerablePower.POWER_ID);
                PCLActions.Top.RemovePower(player,player, FrailPower.POWER_ID);
                this.applyPowers();
                this.calculateCardDamage(enemy);

                PCLActions.Bottom.DealCardDamage(this, enemy, AttackEffects.PUNCH).forEach(d -> d.AddCallback(() ->
                        PCLActions.Bottom.Add(new ShakeScreenAction(0.3f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED))));
                PCLCombatStats.onLosingHP.Unsubscribe(this);
                PCLCombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
            }
        });
    }

    @Override
    public int OnLosingHP(int damageAmount) {
        if (damageAmount > 0) {
            PCLCombatStats.onLosingHP.Unsubscribe(this);
            PCLCombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
            PCLGameEffects.Queue.ShowCardBriefly(makeStatEquivalentCopy());
        }
        return damageAmount;
    }
}