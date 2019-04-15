package eatyourbeets.cards.animator;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;
import eatyourbeets.actions.VariableDiscardAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

import java.util.ArrayList;

public class Layla extends AnimatorCard
{
    public static final String ID = CreateFullID(Layla.class.getSimpleName());

    public Layla()
    {
        super(ID, 0, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(3,0, 1);

        AddExtendedDescription();
        SetSynergy(Synergies.Chaika);
    }

//    @SuppressWarnings("SuspiciousNameCombination")
//    @Override
//    public void triggerOnExhaust()
//    {
//        super.triggerOnExhaust();
//
//        AbstractPlayer p = AbstractDungeon.player;
//        AbstractMonster randomMonster = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
//        if (randomMonster != null)
//        {
//            AbstractDungeon.actionManager.addToBottom(new VFXAction(new PotionBounceEffect(p.hb.cY, p.hb.cX, randomMonster.hb.cX, this.hb.cY), 0.3F));
//        }
//
//        AbstractDungeon.actionManager.addToBottom(new BouncingFlaskAction(randomMonster, this.magicNumber, 3));
//    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        AbstractDungeon.actionManager.addToBottom(new VariableDiscardAction(p, BaseMod.MAX_HAND_SIZE, m, this::OnDiscard));

//        if (HasActiveSynergy())
//        {
//            GameActionsHelper.DrawCard(p, 1);
//        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(2);
        }
    }

    private void OnDiscard(Object state, ArrayList<AbstractCard> discarded)
    {
        AbstractMonster m = Utilities.SafeCast(state, AbstractMonster.class);
        if (state == null || discarded == null)
        {
            return;
        }

        boolean hasSynergy = HasActiveSynergy();
        AbstractPlayer p = AbstractDungeon.player;


        for (int i = 0; i < discarded.size(); i++)
        {
            GameActionsHelper.DamageTargetPiercing(p, m, this, AbstractGameAction.AttackEffect.BLUNT_LIGHT);

            if (hasSynergy)
            {
                GameActionsHelper.ApplyPower(p, m, new PoisonPower(m, p, this.magicNumber), this.magicNumber);
            }
        }
    }
}