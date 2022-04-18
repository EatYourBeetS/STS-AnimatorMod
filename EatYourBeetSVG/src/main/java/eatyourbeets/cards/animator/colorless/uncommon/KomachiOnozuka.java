package eatyourbeets.cards.animator.colorless.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.relics.animator.ShinigamiFerry;
import eatyourbeets.relics.animator.unnamedReign.AncientMedallion;
import eatyourbeets.relics.animator.unnamedReign.UnnamedReignRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class KomachiOnozuka extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KomachiOnozuka.class)
            .SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Piercing)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.TouhouProject);

    private static final AbstractRelic relicReward = new ShinigamiFerry();
    private static final EYBCardTooltip tooltip = new EYBCardTooltip(relicReward.name, relicReward.description);

    public KomachiOnozuka()
    {
        super(DATA);

        Initialize(11, 0, 2, 0);
        SetUpgrade(3, 0, 0, 0);

        SetAffinity_Dark(1, 0, 1);
        SetAffinity_Green(1, 0, 1);

        SetObtainableInCombat(false);
    }

    @Override
    public void initializeDescription()
    {
        super.initializeDescription();

        if (cardText != null)
        {
            tooltips.add(tooltip);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.SFX(SFX.ATTACK_REAPER, 0.95f, 1.05f);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HORIZONTAL)
        .SetSoundPitch(0, 0)
        .AddCallback(enemy ->
        {
            AbstractRoom room = AbstractDungeon.getCurrRoom();
            if ((room instanceof MonsterRoomElite || room instanceof MonsterRoomBoss)
                    && GameUtilities.IsFatal(enemy, false)
                    && CombatStats.TryActivateLimited(cardID))
            {
                ObtainReward();
            }
        });
        GameActions.Bottom.ApplyVulnerable(p, m, magicNumber);
    }

    public void ObtainReward()
    {
        if (UnnamedReignRelic.IsEquipped())
        {
            GameUtilities.GetCurrentRoom(true).addRelicToRewards(new AncientMedallion());
        }
        else
        {
            GameUtilities.GetCurrentRoom(true).addRelicToRewards(relicReward.makeCopy());
        }
    }
}

