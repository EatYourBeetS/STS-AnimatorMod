package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.vfx.combat.ThrowDaggerEffect;
import eatyourbeets.interfaces.OnCallbackSubscriber;
import eatyourbeets.resources.Resources_Animator;
import eatyourbeets.interfaces.metadata.Hidden;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.utilities.RandomizedList;
import eatyourbeets.powers.PlayerStatistics;
import patches.AbstractEnums;

public abstract class ThrowingKnife extends AnimatorCard implements Hidden, OnCallbackSubscriber
{
    public static final String ID = Register(ThrowingKnife.class.getSimpleName());

    private static RandomizedList<ThrowingKnife> subTypes = null;

    public ThrowingKnife(String id)
    {
        super(staticCardData.get(id), id, Resources_Animator.GetCardImage(ID), 0, CardType.ATTACK, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.ENEMY);

        this.tags.add(AbstractEnums.CardTags.PURGE);
    }

    public static AbstractCard GetRandomCard()
    {
        if (subTypes == null)
        {
            subTypes = new RandomizedList<>();
            subTypes.Add(new ThrowingKnife_0());
            subTypes.Add(new ThrowingKnife_1());
            subTypes.Add(new ThrowingKnife_2());
        }

        if (AbstractDungeon.cardRng != null)
        {
            return subTypes.Retrieve(AbstractDungeon.cardRng, false).makeCopy();
        }
        else
        {
            return subTypes.Retrieve(new Random(), false).makeCopy();
        }
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActionsHelper.DelayedAction(this);
    }

    public void OnCallback(Object state, AbstractGameAction action)
    {
        if (state == this)
        {
            AbstractDungeon.player.discardPile.removeCard(this);
            this.freeToPlayOnce = true;
            this.purgeOnUse = true;
            this.applyPowers();
            this.use(AbstractDungeon.player, null);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        if (m == null || m.isDeadOrEscaped())
        {
            m = PlayerStatistics.GetRandomEnemy(true);
        }

        if (m != null)
        {
            GameActionsHelper.SetOrder(GameActionsHelper.Order.Top);
            {
                AddSecondaryEffect(p, m);

                GameActionsHelper.AddToDefault(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), true));
                if (m.hb != null)
                {
                    GameActionsHelper.AddToDefault(new VFXAction(new ThrowDaggerEffect(m.hb.cX, m.hb.cY)));
                }
            }
            GameActionsHelper.ResetOrder();
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(3);
        }
    }

    protected abstract void AddSecondaryEffect(AbstractPlayer p, AbstractMonster m);
}