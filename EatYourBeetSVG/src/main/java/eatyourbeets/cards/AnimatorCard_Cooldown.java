package eatyourbeets.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameUtilities;

public abstract class AnimatorCard_Cooldown extends AnimatorCard
{
    protected abstract int GetBaseCooldown();

    protected AnimatorCard_Cooldown(String id, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target)
    {
        super(id, cost, type, color, rarity, target);
    }

    protected AnimatorCard_Cooldown(String id, int cost, CardType type, CardRarity rarity, CardTarget target)
    {
        super(id, cost, type, rarity, target);
    }

    @Override
    protected void Initialize(int baseDamage, int baseBlock)
    {
        super.Initialize(baseDamage, baseBlock, -1, GetBaseCooldown());
    }

    @Override
    protected void Initialize(int baseDamage, int baseBlock, int baseMagicNumber)
    {
        super.Initialize(baseDamage, baseBlock, baseMagicNumber, GetBaseCooldown());
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (ProgressCooldown())
        {
            OnCooldownCompleted(AbstractDungeon.player, AbstractDungeon.getCurrRoom().monsters.getRandomMonster(true));
        }
    }

    protected void UpgradeCooldown(int amount)
    {
        secondaryValue += amount;
        baseSecondaryValue = secondaryValue;

        if (secondaryValue < 0)
        {
            baseSecondaryValue = secondaryValue = 0;
            isSecondaryValueModified = true;
        }
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();
        this.isSecondaryValueModified = (this.secondaryValue == 0);
        initializeDescription();
    }

    protected boolean ProgressCooldown()
    {
        return ProgressCooldown(1);
    }

    protected boolean ProgressCooldown(int amount)
    {
        boolean activate;
        int newValue;
        if (secondaryValue <= 0)
        {
            newValue = GetBaseCooldown();
            activate = true;
        }
        else
        {
            newValue = Math.max(0, secondaryValue - amount);
            activate = false;
        }

        for (AbstractCard c : GameUtilities.GetAllInBattleInstances(this))
        {
            AnimatorCard_Cooldown card = (AnimatorCard_Cooldown) c;
            card.baseSecondaryValue = card.secondaryValue = newValue;
            //card.applyPowers();
        }

        this.baseSecondaryValue = this.secondaryValue = newValue;

        return activate;
    }

    protected int GetCurrentCooldown()
    {
        return secondaryValue;
    }

    protected abstract void OnCooldownCompleted(AbstractPlayer p, AbstractMonster m);
}
