package eatyourbeets.cards.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.RegrowPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.OnDamageAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Shalltear extends AnimatorCard
{
    public static final String ID = CreateFullID(Shalltear.class.getSimpleName());

//    private boolean returnToHand;

    public Shalltear()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.SELF_AND_ENEMY);

        Initialize(6,0, 1);

        AddExtendedDescription();

        SetSynergy(Synergies.Overlord);
    }

//    @Override
//    public void onMoveToDiscard()
//    {
//        super.onMoveToDiscard();
//
//        if (returnToHand)
//        {
//            AbstractPlayer p = AbstractDungeon.player;
//            GameActionsHelper.AddToBottom(new MoveSpecificCardAction(this, p.hand, p.discardPile));
//            returnToHand = false;
//        }
//    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        DamageAction damageAction = new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.NONE);
        GameActionsHelper.AddToBottom(new VFXAction(new BiteEffect(m.hb.cX, m.hb.cY - 40.0F * Settings.scale, Color.SCARLET.cpy()), 0.3F));
        GameActionsHelper.AddToBottom(new OnDamageAction(m, damageAction, this::OnDamage, this, false));

        if (HasActiveSynergy())
        {
            GameActionsHelper.ApplyPower(p, m, new StrengthPower(m, -1), -1);

            if (!m.hasPower(ArtifactPower.POWER_ID))
            {
                GameActionsHelper.ApplyPower(p, p, new StrengthPower(p, 1), 1);
            }
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

    private void OnDamage(Object state, AbstractMonster monster)
    {
        if (state == this && monster != null && !monster.hasPower(RegrowPower.POWER_ID))
        {
            if ((monster.isDying || monster.currentHealth <= 0) && !monster.halfDead)
            {
                GameActionsHelper.GainEnergy(1);
                AbstractDungeon.player.heal(5, true);
                //returnToHand = true;
            }
        }
    }
}