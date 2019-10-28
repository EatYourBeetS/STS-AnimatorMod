package eatyourbeets.cards.animator;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.unique.BouncingFlaskAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.PotionBounceEffect;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.actions.common.VariableDiscardAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

import java.util.ArrayList;

public class Layla extends AnimatorCard
{
    public static final String ID = Register(Layla.class.getSimpleName(), EYBCardBadge.Discard);

    public Layla()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(3, 0, 2);

        SetSynergy(Synergies.Chaika);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DamageTargetPiercing(p, m, this, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        GameActionsHelper.AddToBottom(new VariableDiscardAction(p, BaseMod.MAX_HAND_SIZE, this, this::OnDiscard));
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(4);
        }
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private void OnDiscard(Object state, ArrayList<AbstractCard> discarded)
    {
        //AbstractMonster m = Utilities.SafeCast(state, AbstractMonster.class);
        if (state == this && discarded != null && discarded.size() > 0)
        {
            AbstractPlayer p = AbstractDungeon.player;
            AbstractMonster randomMonster = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
            if (randomMonster != null)
            {
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new PotionBounceEffect(p.hb.cY, p.hb.cX, randomMonster.hb.cX, randomMonster.hb.cY), 0.3F));
            }

            GameActionsHelper.AddToBottom(new BouncingFlaskAction(randomMonster, this.magicNumber, discarded.size()));
        }
    }
}