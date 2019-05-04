package eatyourbeets.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public abstract class AnimatorCard_Cooldown extends AnimatorCard// extends AnimatorCard_SavableInteger
{
    protected abstract int GetBaseCooldown();

    protected AnimatorCard_Cooldown(String id, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target)
    {
        super(id, cost, type, color, rarity, target);
        this.baseSecondaryValue = this.secondaryValue = GetBaseCooldown();
    }

    protected AnimatorCard_Cooldown(String id, int cost, CardType type, CardRarity rarity, CardTarget target)
    {
        super(id, cost, type, rarity, target);
        this.baseSecondaryValue = this.secondaryValue = GetBaseCooldown();
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

    @Override
    public void applyPowers()
    {
        super.applyPowers();
        this.isSecondaryValueModified = (this.secondaryValue == 0);
        initializeDescription();
    }

    protected boolean ProgressCooldown()
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
            newValue = secondaryValue - 1;
            activate = false;
        }

        for (AbstractCard c : GetAllInBattleInstances.get(this.uuid))
        {
            AnimatorCard_Cooldown card = (AnimatorCard_Cooldown)c;
            card.baseSecondaryValue = card.secondaryValue = newValue;
            //card.applyPowers();
        }

        this.baseSecondaryValue = this.secondaryValue = newValue;

        return activate;
    }

    protected abstract void OnCooldownCompleted(AbstractPlayer p, AbstractMonster m);
}
