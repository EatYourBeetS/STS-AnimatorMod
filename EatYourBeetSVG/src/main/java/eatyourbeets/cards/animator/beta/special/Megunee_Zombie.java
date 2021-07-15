package eatyourbeets.cards.animator.beta.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Megunee_Zombie extends AnimatorCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final EYBCardData DATA = Register(Megunee_Zombie.class).SetAttack(-1, CardRarity.SPECIAL, EYBAttackType.Normal, EYBCardTarget.Random).SetColor(CardColor.COLORLESS);

    private int turns;

    public Megunee_Zombie()
    {
        super(DATA);

        Initialize(13, 0, 6, 10);
        SetUpgrade(3, 0, 0);
        SetAffinity_Red(0, 0, 3);
        SetAffinity_Dark(2, 0, 0);

        SetHealing(true);
        SetExhaust(true);
        SetAutoplay(true);
        SetMultiDamage(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        int stacks = GameUtilities.UseXCostEnergy(this);
        int totalHeal = 0;

        if (stacks > 0)
        {
            for (int i=0; i<stacks; i++)
            {
                GameActions.Bottom.DealDamageToRandomEnemy(this, AbstractGameAction.AttackEffect.NONE)
                .SetDamageEffect(e ->
                        {
                            GameEffects.List.Add(new BiteEffect(e.hb.cX, e.hb.cY - 40f * Settings.scale, Color.BROWN.cpy()));
                            return 0f;
                        }
                )
                .AddCallback(enemy ->
                {
                    if (GameUtilities.IsFatal(enemy, false) && CombatStats.TryActivateLimited(cardID))
                    {
                        GameActions.Bottom.Heal(magicNumber);
                    }
                });
            }
        }
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        turns = rng.random(0, 3);
        CombatStats.onStartOfTurnPostDraw.Subscribe(this);
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        if (player.exhaustPile.contains(this))
        {
            if (turns <= 0)
            {
                GameActions.Bottom.MoveCard(this, player.exhaustPile, player.hand)
                        .ShowEffect(false, false);
                CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
            }
            else
            {
                turns -= 1;
            }
        }
        else
        {
            CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
        }
    }
}