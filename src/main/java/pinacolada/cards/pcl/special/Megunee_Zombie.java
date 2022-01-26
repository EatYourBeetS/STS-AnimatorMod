package pinacolada.cards.pcl.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class Megunee_Zombie extends PCLCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final PCLCardData DATA = Register(Megunee_Zombie.class).SetAttack(-1, CardRarity.SPECIAL, PCLAttackType.Brutal, PCLCardTarget.Random).SetColor(CardColor.COLORLESS);

    private int turns;

    public Megunee_Zombie()
    {
        super(DATA);

        Initialize(13, 0, 6, 10);
        SetUpgrade(3, 0, 0);
        SetAffinity_Red(0, 0, 0);
        SetAffinity_Dark(1, 0, 2);

        SetHealing(true);
        SetExhaust(true);
        SetAutoplay(true);
        SetMultiDamage(true);
    }

    @Override
    public void update()
    {
        super.update();

        PCLGameUtilities.IncreaseHitCount(this, EnergyPanel.getCurrentEnergy(), true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        int stacks = PCLGameUtilities.UseXCostEnergy(this);
        int totalHeal = 0;

        if (stacks > 0)
        {
            PCLActions.Bottom.DealCardDamageToRandomEnemy(this, AttackEffects.NONE)
                    .forEach(d -> d.SetDamageEffect(e ->
                            {
                                PCLGameEffects.List.Add(new BiteEffect(e.hb.cX, e.hb.cY - 40f * Settings.scale, Color.BROWN.cpy()));
                                return 0f;
                            }
                    )
                            .AddCallback(enemy ->
                            {
                                if (PCLGameUtilities.IsFatal(enemy, false) && CombatStats.TryActivateLimited(cardID))
                                {
                                    PCLActions.Bottom.Heal(magicNumber);
                                }
                            }));
        }
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        turns = rng.random(0, 3);
        PCLCombatStats.onStartOfTurnPostDraw.Subscribe(this);
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        if (player.exhaustPile.contains(this))
        {
            if (turns <= 0)
            {
                PCLActions.Bottom.MoveCard(this, player.exhaustPile, player.hand)
                        .ShowEffect(false, false);
                PCLCombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
            }
            else
            {
                turns -= 1;
            }
        }
        else
        {
            PCLCombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
        }
    }
}