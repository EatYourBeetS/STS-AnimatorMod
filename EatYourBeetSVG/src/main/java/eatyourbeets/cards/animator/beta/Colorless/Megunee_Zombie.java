package eatyourbeets.cards.animator.beta.Colorless;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.misc.CardMods.AfterLifeMod;
import eatyourbeets.ui.common.ControllableCard;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class Megunee_Zombie extends AnimatorCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final EYBCardData DATA = Register(Megunee_Zombie.class).SetAttack(-1, CardRarity.SPECIAL, EYBAttackType.Normal, EYBCardTarget.Random).SetColor(CardColor.COLORLESS);

    public Megunee_Zombie()
    {
        super(DATA);

        Initialize(15, 0, 2, 10);
        SetUpgrade(3, 0, 0);
        SetScaling(0, 0, 3);

        SetHealing(true);
        SetExhaust(true);
        SetMultiDamage(true);

        AfterLifeMod.Add(this);

        SetSynergy(Synergies.Gakkougurashi);
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        boolean playable = super.cardPlayable(m);

        if (playable)
        {
            if (JUtils.Find(AbstractDungeon.actionManager.cardsPlayedThisTurn, Megunee_Zombie.class::isInstance) != null)
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.PlayCard(this, player.hand, null)
                .SpendEnergy(true)
                .AddCondition(AbstractCard::hasEnoughEnergy);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        int stacks = GameUtilities.UseXCostEnergy(this);
        int totalHeal = 0;

        if (stacks > 0)
        {
            for (int i=0; i<stacks; i++)
            {
                GameActions.Bottom.DealDamageToRandomEnemy(this, AbstractGameAction.AttackEffect.NONE)
                        .SetDamageEffect(e -> GameEffects.List.Add(new BiteEffect(e.hb.cX, e.hb.cY - 40f * Settings.scale, Color.BROWN.cpy())));

                if (totalHeal <= secondaryValue)
                {
                    GameActions.Bottom.Heal(magicNumber);
                    totalHeal += 2;
                }
            }
        }
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        if (player.exhaustPile.contains(this) && cardPlayable(null))
        {
            GameActions.Bottom.Callback(() ->
            {
                AfterLifeMod.PlayFromAfterlife(new ControllableCard(this));
            });
        }
    }
}