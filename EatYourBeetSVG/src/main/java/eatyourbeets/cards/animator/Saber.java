package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.ModifyMagicNumberAction;
import eatyourbeets.actions.MoveSpecificCardAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Saber extends AnimatorCard
{
    public static final String ID = CreateFullID(Saber.class.getSimpleName());

    public Saber()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);

        Initialize(11,0,3);

        SetSynergy(Synergies.Fate);
    }

    @Override
    public void onMoveToDiscard()
    {
        super.onMoveToDiscard();
        AbstractPlayer player = AbstractDungeon.player;
        AbstractDungeon.actionManager.addToBottom(new MoveSpecificCardAction(this, player.drawPile, player.discardPile, false));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));

        if (HasActiveSynergy())
        {
            if (this.magicNumber > 1)
            {
                AbstractDungeon.actionManager.addToBottom(new ModifyMagicNumberAction(this.uuid, -1));
            }
            else
            {
                this.purgeOnUse = true;
                Excalibur excalibur = new Excalibur();
                if (upgraded)
                {
                    excalibur.upgrade();
                }
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(excalibur));
            }
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(2);
            //upgradeMagicNumber(-1);
        }
    }
}