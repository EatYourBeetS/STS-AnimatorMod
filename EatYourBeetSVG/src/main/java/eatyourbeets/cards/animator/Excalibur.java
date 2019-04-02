package eatyourbeets.cards.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.ModifyDamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.VerticalImpactEffect;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;

public class Excalibur extends AnimatorCard
{
    public static final String ID = CreateFullID(Excalibur.class.getSimpleName());

    public Excalibur()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.SPECIAL, CardTarget.ALL_ENEMY);

        Initialize(8,0);

        this.retain = true;
        this.isMultiDamage = true;
        SetSynergy(Synergies.Fate);
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();
        this.retain = true;
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        super.triggerOnEndOfTurnForPlayingCard();

        this.retain = true;
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp)
    {
        for (AbstractCard card : player.hand.group)
        {
            if (card != this && card.type == CardType.ATTACK && card.damage > 0)
            {
                if (card.cardID.equals(this.cardID))
                {
                    tmp += card.baseDamage;
                }
                else
                {
                    tmp += card.damage;
                }
            }
        }

        return super.calculateModifiedCardDamage(player, mo, tmp);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        int damageUpgrade = 0;
        for (AbstractCard c : p.hand.group)
        {
            if (c.type == CardType.ATTACK && c != this)
            {
                damageUpgrade += c.damage;
                AbstractDungeon.actionManager.addToBottom(new ExhaustSpecificCardAction(c, p.hand, true));
            }
        }

        GameActionsHelper.AddToBottom(new ModifyDamageAction(this.uuid, damageUpgrade));

        GameActionsHelper.AddToBottom(new VFXAction(new BorderLongFlashEffect(Color.GOLD)));
        for (AbstractCreature m1 : PlayerStatistics.GetCurrentEnemies(true))
        {
            GameActionsHelper.AddToBottom(new VFXAction(new VerticalImpactEffect(m1.hb_x, m1.hb_y)));
        }
        GameActionsHelper.DamageAllEnemies(AbstractDungeon.player, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HEAVY);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(8);
            //upgradeBaseCost(1);
        }
    }
}