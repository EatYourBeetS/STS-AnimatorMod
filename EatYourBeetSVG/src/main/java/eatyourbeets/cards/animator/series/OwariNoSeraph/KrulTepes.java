package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.BloodVial;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.relics.UnnamedReign.AncientMedallion;
import eatyourbeets.relics.UnnamedReign.UnnamedReignRelic;
import eatyourbeets.relics.animator.ExquisiteBloodVial;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class KrulTepes extends AnimatorCard
{
    public static final String ID = Register(KrulTepes.class.getSimpleName(), EYBCardBadge.Special);

    private static final AbstractRelic relicReward = new BloodVial();

    public KrulTepes()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(14,0, 2);

        AddExtendedDescription();

        SetSynergy(Synergies.OwariNoSeraph);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        if (m != null)
        {
            GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE)
            .SetDamageEffect(e -> AbstractDungeon.effectList.add(new BiteEffect(e.hb.cX, e.hb.cY - 40.0F * Settings.scale, Color.SCARLET.cpy())))
            .AddCallback(enemy ->
            {
                AbstractRoom room = AbstractDungeon.getCurrRoom();
                if ((room instanceof MonsterRoomElite || room instanceof MonsterRoomBoss)
                    && GameUtilities.TriggerOnKill(enemy, false)
                    && EffectHistory.TryActivateLimited(cardID))
                {
                    ObtainReward();
                }
            });

            GameActions.Bottom.ApplyWeak(p, m, magicNumber);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {          
            upgradeDamage(4);
        }
    }

//    public boolean CanGetReward()
//    {
//        AbstractRoom room = AbstractDungeon.getCurrRoom();
//        for (RewardItem r : room.rewards)
//        {
//            if (r.relic != null && r.relic.relicId.equals(relicReward.relicId))
//            {
//                return false;
//            }
//        }
//
//        return true;
//    }

    public void ObtainReward()
    {
        int ownedRelics = 0;
        ArrayList<AbstractRelic> relics = AbstractDungeon.player.relics;
        for (AbstractRelic relic : relics)
        {
            if (relic.relicId.equals(relicReward.relicId))
            {
                ownedRelics += 1;
            }
            else if (relic.relicId.equals(ExquisiteBloodVial.ID))
            {
                ownedRelics = -1;
                break;
            }
        }

        if (ownedRelics > 3 && (ownedRelics >= 5 || AbstractDungeon.cardRandomRng.randomBoolean()))
        {
            AbstractDungeon.getCurrRoom().addRelicToRewards(new ExquisiteBloodVial());
        }
        else if (UnnamedReignRelic.IsEquipped())
        {
            AbstractDungeon.getCurrRoom().addRelicToRewards(new AncientMedallion());
        }
        else
        {
            AbstractDungeon.getCurrRoom().addRelicToRewards(relicReward.makeCopy());
        }
    }
}