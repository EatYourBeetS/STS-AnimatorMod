package eatyourbeets.cards.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ModifyDamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.actions.animator.EntomaAction;
import eatyourbeets.actions.common.OnTargetDeadAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Entoma extends AnimatorCard//_SavableInteger implements CustomSavable<Integer>
{
    public static final String ID = CreateFullID(Entoma.class.getSimpleName());

    private static final int ORIGINAL_DAMAGE = 7;
    private static final int ORIGINAL_MAGIC_NUMBER = 3;

    public Entoma()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(ORIGINAL_DAMAGE,0,ORIGINAL_MAGIC_NUMBER);

        AddExtendedDescription();

        SetUnique(true);
        SetSynergy(Synergies.Overlord);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (m == null)
        {
            return;
        }

        GameActionsHelper.AddToBottom(new VFXAction(new BiteEffect(m.hb.cX, m.hb.cY - 40.0F * Settings.scale, Color.SCARLET.cpy()), 0.3F));
        DamageAction damageAction = new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        GameActionsHelper.AddToBottom(new OnTargetDeadAction(m, damageAction, new EntomaAction(this)));
        GameActionsHelper.AddToBottom(new ApplyPowerAction(m, p, new PoisonPower(m, p, this.magicNumber), this.magicNumber));
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        if (this.baseDamage > 0)
        {
            GameActionsHelper.AddToBottom(new ModifyDamageAction(this.uuid, -1));
        }
    }

    @Override
    public boolean canUpgrade()
    {
        return true;
    }

    @Override
    public void upgrade()
    {
        this.timesUpgraded += 1;

        this.upgradeDamage(1);

        if (timesUpgraded % 3 == 0)
        {
            upgradeMagicNumber(1);
        }
        this.upgradedMagicNumber = true;

        this.upgraded = true;
        this.name = cardStrings.NAME + "+" + this.timesUpgraded;
        this.initializeTitle();
    }
}