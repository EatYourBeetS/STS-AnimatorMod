package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import eatyourbeets.AnimatorResources;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;

public class ShichikaKyotouryuu extends AnimatorCard implements AnimatorResources.Hidden
{
    public static final String ID = CreateFullID(ShichikaKyotouryuu.class.getSimpleName());

    public ShichikaKyotouryuu()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.SPECIAL, CardTarget.ENEMY);

        Initialize(1, 0, 4);

        this.exhaust = true;
        this.isEthereal = true;

        SetSynergy(Synergies.Katanagatari);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.AddToBottom(new VFXAction(new FlashAtkImgEffect(m.hb.cX, m.hb.cY - 40.0F * Settings.scale, AbstractGameAction.AttackEffect.SLASH_HEAVY), 0.1F));

        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.BLUNT_HEAVY);

        GameActionsHelper.AddToBottom(new VFXAction(new FlashAtkImgEffect(m.hb.cX, m.hb.cY - 40.0F * Settings.scale, AbstractGameAction.AttackEffect.SLASH_HEAVY), 0.1F));

        if (PlayerStatistics.GetDexterity(p) >= 3)
        {
            GameActionsHelper.GainEnergy(1);
            GameActionsHelper.DrawCard(p, 1);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(1);
        }
    }
}

/*

package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import eatyourbeets.AnimatorResources;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;

public class Shichika extends AnimatorCard
{
    public static final String ID = CreateFullID(Shichika.class.getSimpleName());

    private boolean transformed = false;

    public Shichika()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(2, 3, 2);

        AddExtendedDescription();

        this.baseSecondaryValue = this.secondaryValue = 4;

        SetSynergy(Synergies.Katanagatari);
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        AbstractPlayer p = AbstractDungeon.player;
        if (PlayerStatistics.GetDexterity(p) >= 4 && PlayerStatistics.GetStrength(p) >= 4)
        {
            if (!transformed)
            {
                this.loadCardImage(AnimatorResources.GetCardImage(ID + "Alt"));
                this.type = CardType.ATTACK;
                this.target = CardTarget.SELF_AND_ENEMY;
                rawDescription = cardStrings.EXTENDED_DESCRIPTION[2];
                initializeDescription();
                transformed = true;
            }
        }
        else
        {
            if (transformed)
            {
                this.loadCardImage(AnimatorResources.GetCardImage(ID));
                this.type = CardType.SKILL;
                this.target = CardTarget.ENEMY;
                rawDescription = cardStrings.DESCRIPTION;
                initializeDescription();
                transformed = false;
            }
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (transformed)
        {
            for (int i = 0; i < this.secondaryValue; i++)
            {
                GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
            }
        }
        else
        {
            GameActionsHelper.ApplyPower(p, p, new StrengthPower(p, 1), 1);
            GameActionsHelper.GainBlock(p, this.block);
        }

        if (HasActiveSynergy())
        {
            GameActionsHelper.ApplyPower(p, p, new ThornsPower(p, this.magicNumber), this.magicNumber);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeBlock(2);
            upgradeMagicNumber(1);
            upgradeDamage(1);
        }
    }
}

*/